package com.zedzak.zednotelite.data.local

import android.util.Log
import com.zedzak.zednotelite.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomNotesRepository(
    private val dao: NoteDao
) : NotesRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        dao.getAllNotes()
            .map { entities -> entities.map { it.toModel() } }

    override suspend fun getNoteById(id: String): Note? =
        dao.getNoteById(id)?.toModel()

    // 🔁 Business name → storage upsert
    override suspend fun addNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    // 🔁 Business name → storage upsert
    override suspend fun updateNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    // 🔁 Business name → storage delete
    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }
}



