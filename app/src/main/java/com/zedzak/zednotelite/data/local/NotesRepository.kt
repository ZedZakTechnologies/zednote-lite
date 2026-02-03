package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow
import com.zedzak.zednotelite.model.Note

interface NotesRepository {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: String): Note?

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)
}



