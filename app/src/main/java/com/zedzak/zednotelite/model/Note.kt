package com.zedzak.zednotelite.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val lastEditedAt: Long,
    val createdAt: Long = lastEditedAt,
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false
) {
    val isPersisted: Boolean
        get() = lastEditedAt > 0
}

fun Note.isEffectivelyEmpty(): Boolean =
    title.isBlank() && content.isBlank()

