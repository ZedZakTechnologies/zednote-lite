package com.zedzak.zednotelite.data.local
import com.zedzak.zednotelite.model.Note
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val content: String,
    val lastEditedAt: Long,
    val createdAt: Long,
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false

)


fun NoteEntity.toNote() = Note(
    id = id.toLong(),
    title = title,
    content = content,
    lastEditedAt = lastEditedAt,
    createdAt = if (createdAt == 0L) lastEditedAt else createdAt,
    isDeleted = isDeleted,
    isPinned = isPinned
)

