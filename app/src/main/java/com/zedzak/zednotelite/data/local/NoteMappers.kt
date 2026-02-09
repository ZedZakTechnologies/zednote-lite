package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.model.Note

fun NoteEntity.toModel(): Note =
    Note(
        id = id,
        title = title,
        content = content,
        createdAt = if (createdAt == 0L) lastEditedAt else createdAt,
        lastEditedAt = lastEditedAt,
        isDeleted = isDeleted,
        isPinned = isPinned
    )

fun Note.toEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        isDeleted = isDeleted,
        isPinned = isPinned
    )
