package com.zedzak.zednotelite.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey

object SettingsKeys {

    val SHOW_WORD_COUNT =
        booleanPreferencesKey("show_word_count")

    val AUTOSAVE_ENABLED =
        booleanPreferencesKey("autosave_enabled")
}
