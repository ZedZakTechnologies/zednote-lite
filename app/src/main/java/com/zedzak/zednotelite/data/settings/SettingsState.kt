package com.zedzak.zednotelite.data.settings
import com.zedzak.zednotelite.model.NoteSortMode
import com.zedzak.zednotelite.model.SortDirection


data class SettingsState(
    val showWordCount: Boolean = true,
    val autosaveEnabled: Boolean = true,
    val sortMode: NoteSortMode = NoteSortMode.LAST_EDITED,
    val sortDirection: SortDirection = SortDirection.DESC
)

