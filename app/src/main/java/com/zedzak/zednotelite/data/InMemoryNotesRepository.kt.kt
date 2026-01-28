package com.zedzak.zednotelite.data

import com.zedzak.zednotelite.model.Note

class InMemoryNotesRepository : NotesDataSource {

    private val notes = mutableListOf<Note>()

    override fun getAllNotes(): List<Note> {
        return notes.toList()
    }

    override fun getNoteById(id: String): Note? {
        return notes.find { it.id == id }
    }

    override fun addNote(note: Note) {
        notes.add(note)
    }

    override fun updateNote(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
    }
}
