package com.zedzak.zednotelite.data.local
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE isDeleted = 0 ")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("UPDATE notes SET isPinned = 1 WHERE id = :noteId")
    suspend fun pinNote(noteId: Long)

    @Query("UPDATE notes SET isPinned = 0 WHERE id = :noteId")
    suspend fun unpinNote(noteId: Long)

    @Query("SELECT isPinned FROM notes WHERE id = :id")
    suspend fun debugIsPinned(id: Long): Int?

}


