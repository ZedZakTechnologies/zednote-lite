package com.zedzak.zednotelite.data.local

class RoomNotesRepository(
    private val dao: NoteDao
) : NotesRepository {

    override suspend fun getNotes(): List<NoteEntity> {
        return dao.getAllNotes()
    }

    override suspend fun addNote(title: String, content: String) {
        dao.insertNote(
            NoteEntity(
                title = title,
                content = content
            )
        )
    }

    override suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }
}
