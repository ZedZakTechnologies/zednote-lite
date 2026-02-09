package com.zedzak.zednotelite.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {

    val SHOW_WORD_COUNT =
        booleanPreferencesKey("show_word_count")

    val AUTOSAVE_ENABLED =
        booleanPreferencesKey("autosave_enabled")

    val SORT_MODE = stringPreferencesKey("sort_mode")
    val SORT_DIRECTION = stringPreferencesKey("sort_direction")

    val SORT_DIRECTION_LAST_EDITED =
        stringPreferencesKey("sort_direction_last_edited")

    val SORT_DIRECTION_CREATED_DATE =
        stringPreferencesKey("sort_direction_created_date")

    val SORT_DIRECTION_TITLE =
        stringPreferencesKey("sort_direction_title")

}
