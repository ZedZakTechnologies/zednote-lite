package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.data.local.NoteEntity

interface NotesRepository {

    suspend fun getNotes(): List<NoteEntity>


    suspend fun deleteNote(note: NoteEntity)
}
