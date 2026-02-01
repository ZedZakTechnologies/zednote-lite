package com.zedzak.zednotelite.data.local

class RoomNotesRepository(
    private val dao: NoteDao
) : NotesRepository {

    override suspend fun getNotes(): List<NoteEntity> {
        return dao.getAllNotes()
    }



    override suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }
}
