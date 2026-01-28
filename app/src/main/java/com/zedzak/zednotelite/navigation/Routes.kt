package com.zedzak.zednotelite.navigation

object Routes {
    const val HOME = "home"
    const val EDITOR = "editor"
    const val EDITOR_WITH_ID = "editor/{noteId}"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val SECURITY = "security"
    fun editorWithId(noteId: String): String {
        return "editor/$noteId"
    }
}


