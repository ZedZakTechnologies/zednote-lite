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

class NotesViewModel(
    private val repository: NotesDataSource
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote: StateFlow<Note?> = _activeNote

    init {
        loadNotes()
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



}
