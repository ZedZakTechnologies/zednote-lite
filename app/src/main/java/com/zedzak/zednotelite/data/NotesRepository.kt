package com.zedzak.zednotelite.data

import com.zedzak.zednotelite.model.Note

object NotesRepository {

    private val notes = mutableListOf<Note>()

    fun getAllNotes(): List<Note> {
        return notes.toList()
    }

    fun getNoteById(id: String): Note? {
        return notes.find { it.id == id }
    }

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun updateNote(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
    }
}
