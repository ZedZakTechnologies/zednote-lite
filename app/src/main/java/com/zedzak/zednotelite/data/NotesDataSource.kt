package com.zedzak.zednotelite.data

import com.zedzak.zednotelite.model.Note

interface NotesDataSource {
    fun getAllNotes(): List<Note>
    fun getNoteById(id: String): Note?
    fun addNote(note: Note)
    fun updateNote(note: Note)
}


