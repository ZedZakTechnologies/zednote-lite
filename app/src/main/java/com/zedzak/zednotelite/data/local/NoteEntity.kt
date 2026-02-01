package com.zedzak.zednotelite.data.local
import com.zedzak.zednotelite.model.Note
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val lastUpdated: Long
)


fun NoteEntity.toNote() = Note(
    id = id.toString(),
    title = title,
    content = content,
    lastUpdated = lastUpdated
)

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    content = content,
    lastUpdated = lastUpdated
)

