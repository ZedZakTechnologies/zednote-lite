package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.data.local.NoteEntity

interface NotesRepository {

    suspend fun getNotes(): List<NoteEntity>

    suspend fun addNote(title: String, content: String)

    suspend fun deleteNote(note: NoteEntity)
}
