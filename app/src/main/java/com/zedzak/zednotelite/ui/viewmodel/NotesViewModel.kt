package com.zedzak.zednotelite.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedzak.zednotelite.data.NotesDataSource
import com.zedzak.zednotelite.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import com.zedzak.zednotelite.data.local.toEntity
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import com.zedzak.zednotelite.data.local.NotesRepository
import com.zedzak.zednotelite.model.isEffectivelyEmpty
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.zedzak.zednotelite.model.NoteSortMode
import kotlinx.coroutines.flow.SharingStarted
import com.zedzak.zednotelite.model.SortDirection

class NotesViewModel(
    private val repository: NotesRepository,
    private val sortModeFlow: StateFlow<NoteSortMode>,
    private val sortDirectionFlow: StateFlow<SortDirection>
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private fun applySort(
        notes: List<Note>,
        mode: NoteSortMode,
        direction: SortDirection
    ): List<Note> {

        val sorted = when (mode) {
            NoteSortMode.LAST_EDITED ->
                notes.sortedBy { it.lastEditedAt }

            NoteSortMode.CREATED_DATE ->
                notes.sortedBy { it.createdAt }

            NoteSortMode.TITLE ->
                notes.sortedBy { it.title.lowercase() }
        }

        return if (direction == SortDirection.DESC) {
            sorted.reversed()
        } else {
            sorted
        }
    }


    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote: StateFlow<Note?> = _activeNote.asStateFlow()

    private val autosaveTrigger =
        MutableSharedFlow<Note>(extraBufferCapacity = 1)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val visibleNotes: StateFlow<List<Note>> =
        combine(
            _notes,
            sortModeFlow,
            sortDirectionFlow,
            searchQuery
        ) { notes, sortMode, sortDirection, query ->

            // 1️⃣ Apply search FIRST
            val searched =
                if (query.isBlank()) {
                    notes
                } else {
                    notes.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.content.contains(query, ignoreCase = true)
                    }
                }

            // 2️⃣ Split pinned / unpinned from SEARCHED set
            val pinned = searched.filter { it.isPinned }
            val unpinned = searched.filterNot { it.isPinned }

            // 3️⃣ Sort each group independently
            val sortedPinned = applySort(pinned, sortMode, sortDirection)
            val sortedUnpinned = applySort(unpinned, sortMode, sortDirection)

            // 4️⃣ Merge (pinned always first)
            sortedPinned + sortedUnpinned
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )




    init {
        //  Notes list pipeline (Room → UI)
        viewModelScope.launch {
            repository.getAllNotes()
                .collect { notes: List<Note> ->
                    _notes.value = notes
                }
        }

        //  Autosave pipeline (updates only)
        viewModelScope.launch {
            autosaveTrigger
                .debounce(400)
                .collect { note ->
                    repository.updateNote(note)
                }
        }
    }

    /* -----------------------------
       Navigation / lifecycle
       ----------------------------- */

    fun createNewNote(): Long {
        val note = Note(
            id = System.currentTimeMillis(),
            title = "",
            content = "",
            lastEditedAt = System.currentTimeMillis(),

            isDeleted = false
        )

        _activeNote.value = note

        viewModelScope.launch {
            repository.addNote(note) // INSERT immediately
        }

        return note.id
    }

    fun openNote(noteId: Long) {
        // If we already have this note (e.g. just created), don’t refetch
        val current = _activeNote.value
        if (current != null && current.id == noteId) return

        viewModelScope.launch {
            _activeNote.value = repository.getNoteById(noteId)
        }
    }





    fun onEditorChanged(note: Note) {
        _activeNote.value = note
        autosaveTrigger.tryEmit(note)
    }

    fun saveAndClose(title: String, content: String) {
        val note = _activeNote.value ?: return

        val updated = note.copy(
            title = title.trim(),
            content = content.trim(),
            lastEditedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            when {
                updated.isEffectivelyEmpty() && note.isPersisted ->
                    repository.deleteNote(note)

                updated.isEffectivelyEmpty() && !note.isPersisted ->
                    Unit // brand-new empty note → ignore

                else ->
                    repository.updateNote(updated)
            }
        }

        _activeNote.value = updated
    }

    fun onBackFromEditor() {
        _activeNote.value = null
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun togglePin(noteId: Long) {
        Log.d("PIN_VM", "togglePin called for id=$noteId")

        viewModelScope.launch {
            val note = repository.getNoteById(noteId) ?: return@launch
            Log.d("PIN_VM", "before update isPinned=${note?.isPinned}")

            if (note == null) return@launch

            if (note.isPinned) {
                repository.unpinNote(noteId)
            } else {
                repository.pinNote(noteId)
            }
        }
    }


}
