package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.data.NotesDataSource
import com.zedzak.zednotelite.model.Note
import java.util.UUID
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.zedzak.zednotelite.data.local.toModel

class RoomNotesDataSource(
    private val dao: NoteDao
) : NotesDataSource {

    override fun getAllNotes(): Flow<List<Note>> =
        dao.getAllNotes().map { it.map { e -> e.toModel() } }

    override suspend fun upsert(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun delete(note: Note) {
        dao.deleteNote(note.toEntity())
    }
}


