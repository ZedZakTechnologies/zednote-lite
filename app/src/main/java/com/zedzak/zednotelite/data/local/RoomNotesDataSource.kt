package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.data.NotesDataSource
import com.zedzak.zednotelite.model.Note
import java.util.UUID

class RoomNotesDataSource(
    private val dao: NoteDao
) : NotesDataSource {

    override suspend fun getAllNotes(): List<Note> =
        dao.getAllNotes().map { it.toNote() }

    override suspend fun getNoteById(id: String): Note? =
        dao.getNoteById(id)?.toNote()

    override suspend fun addNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun createNote(): String {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = "",
            content = "",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertNote(note.toEntity())
        return note.id
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }
}

