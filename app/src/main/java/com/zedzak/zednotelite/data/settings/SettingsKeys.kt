package com.zedzak.zednotelite.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {

    val SHOW_WORD_COUNT =
        booleanPreferencesKey("show_word_count")

    val AUTOSAVE_ENABLED =
        booleanPreferencesKey("autosave_enabled")

    val SORT_MODE = stringPreferencesKey("sort_mode")
}
