package com.zedzak.zednotelite.navigation

object Routes {
    const val HOME = "home"
    const val EDITOR = "editor/{noteId}"

    fun editor(noteId: Long) = "editor/$noteId"


}


