package com.zedzak.zednotelite.data

import com.zedzak.zednotelite.model.Note

interface NotesDataSource {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNoteById(id: String): Note?
    suspend fun addNote(note: Note)
    suspend fun createNote(): String
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: Note)
}



