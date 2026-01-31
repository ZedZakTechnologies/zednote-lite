package com.zedzak.zednotelite.data.local
import com.zedzak.zednotelite.model.Note
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)


fun NoteEntity.toNote() = Note(
    id = id.toString(),
    title = title,
    content = content,
    lastUpdated = createdAt
)

fun Note.toEntity() = NoteEntity(
    id = id.toLongOrNull() ?: 0L,
    title = title,
    content = content,
    createdAt = lastUpdated
)

