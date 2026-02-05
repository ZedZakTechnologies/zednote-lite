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


class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote: StateFlow<Note?> = _activeNote.asStateFlow()

    private val autosaveTrigger =
        MutableSharedFlow<Note>(extraBufferCapacity = 1)

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

    fun createNewNote(): String {
        val note = Note(
            id = UUID.randomUUID().toString(),
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

    fun openNote(noteId: String) {
        _activeNote.value = null
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
}
