package com.zedzak.zednotelite.data.settings
import com.zedzak.zednotelite.model.NoteSortMode

data class SettingsState(
    val showWordCount: Boolean = true,
    val autosaveEnabled: Boolean = true,
    val sortMode: NoteSortMode = NoteSortMode.LAST_EDITED
)