package com.zedzak.zednotelite.data.local

import com.zedzak.zednotelite.model.Note

fun NoteEntity.toModel(): Note =
    Note(
        id = id,
        title = title,
        content = content,
        lastEditedAt = lastEditedAt,
        isDeleted = isDeleted
    )

fun Note.toEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        lastEditedAt = lastEditedAt,
        isDeleted = isDeleted
    )
