package com.zedzak.zednotelite.viewmodel

import androidx.lifecycle.ViewModel
import com.zedzak.zednotelite.data.NotesRepository
import com.zedzak.zednotelite.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class NotesViewModel : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _activeNote = MutableStateFlow<Note?>(null)
    val activeNote: StateFlow<Note?> = _activeNote

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _notes.value = NotesRepository.getAllNotes()
    }

    fun createNewNote(): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = "",
            content = "",
            lastUpdated = System.currentTimeMillis()
        )
        NotesRepository.addNote(note)
        loadNotes()
        _activeNote.value = note
        return note
    }

    fun openNote(noteId: String) {
        _activeNote.value = NotesRepository.getNoteById(noteId)
    }

    fun saveNote(title: String, content: String) {
        val now = System.currentTimeMillis()
        val current = _activeNote.value

        if (current == null) {
            val note = Note(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                lastUpdated = now
            )
            NotesRepository.addNote(note)
        } else {
            NotesRepository.updateNote(
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

    fun clearActiveNote() {
        _activeNote.value = null
    }
}
