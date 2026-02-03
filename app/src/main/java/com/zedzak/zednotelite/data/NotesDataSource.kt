package com.zedzak.zednotelite.data

import com.zedzak.zednotelite.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesDataSource {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun upsert(note: Note)
    suspend fun delete(note: Note)
}




