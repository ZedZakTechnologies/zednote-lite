package com.zedzak.zednotelite.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zedzak.zednotelite.data.InMemoryNotesRepository
import com.zedzak.zednotelite.data.NotesDataSource
import com.zedzak.zednotelite.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class NotesViewModel(
    private val repository: NotesDataSource = InMemoryNotesRepository()
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote: StateFlow<Note?> = _activeNote

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _notes.value = repository.getAllNotes()
    }

    fun createNewNote(): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = "",
            content = "",
            lastUpdated = System.currentTimeMillis()
        )
        repository.addNote(note)
        loadNotes()
        _activeNote.value = note
        return note
    }

    fun openNote(noteId: String) {
        _activeNote.value = repository.getNoteById(noteId)
    }

    fun saveNote(title: String, content: String) {
        val now = System.currentTimeMillis()
        val current = _activeNote.value

        if (current == null) {
            repository.addNote(
                Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    lastUpdated = now
                )
            )
        } else {
            repository.updateNote(
                current.copy(
                    title = title,
                    content = content,
                    lastUpdated = now
                )
            )
        }

        loadNotes()
        _activeNote.value = null
    }
}
