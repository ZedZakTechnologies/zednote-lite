package com.zedzak.zednotelite.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val lastEditedAt: Long,
    val isDeleted: Boolean = false
)
