package com.zedzak.zednotelite.ui.viewmodel

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


class NotesViewModel(
    private val repository: NotesDataSource
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote = _activeNote.asStateFlow()
    private val autosaveTrigger =
        MutableSharedFlow<Note>(
            extraBufferCapacity = 1
        )

    init {
        viewModelScope.launch {
            autosaveTrigger
                .debounce(400)
                .collect { draft ->
                    repository.updateNote(draft)
                }
        }
    }



    private fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAllNotes()
        }
    }


// Called when starting a brand new note (no ID yet)
    fun openNote(noteId: String) {
        viewModelScope.launch {
            _activeNote.value = repository.getNoteById(noteId)
        }
    }

    fun createNewNote(): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = "",
            content = "",
            lastEditedAt = System.currentTimeMillis()
        )

        _activeNote.value = note
        return note
    }

    // Called when starting a brand new note (no ID yet)
    fun startNewNote(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val id = repository.createNote()
            openNote(id)
            onCreated(id)
        }
    }


    fun saveNote(title: String, content: String) {
        if (title.isBlank() && content.isBlank()) {
            return // do nothing
        }

        viewModelScope.launch {
            val current = _activeNote.value

            if (current == null) {
                // Create
                val note = Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    lastEditedAt = System.currentTimeMillis()
                )
                repository.addNote(note)
                _activeNote.value = note
            } else {
                // Update
                val updated = current.copy(
                    title = title,
                    content = content,
                    lastEditedAt = System.currentTimeMillis()
                )
                repository.updateNote(updated)
                _activeNote.value = updated
            }

            loadNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _activeNote.value = null
            loadNotes()
        }
    }

    private val editorState = MutableStateFlow(
        EditorDraft(title = "", content = "")
    )

    data class EditorDraft(
        val title: String,
        val content: String
    )

    fun onTitleChanged(title: String) {
        editorState.update { it.copy(title = title) }
    }

    fun onContentChanged(content: String) {
        editorState.update { it.copy(content = content) }
    }

    private suspend fun autosave(draft: EditorDraft) {
        val note = _activeNote.value ?: return

        if (
            note.title == draft.title &&
            note.content == draft.content
        ) return

        val updated = note.copy(
            title = draft.title,
            content = draft.content,
            lastEditedAt = System.currentTimeMillis()
        )

        repository.updateNote(updated)
        _activeNote.value = updated
    }

    fun onEditorChanged(note: Note) {
        _activeNote.value = note
        autosaveTrigger.tryEmit(note)
    }




}
