package com.zedzak.zednotelite.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    val settingsFlow: Flow<SettingsState> =
        dataStore.data.map { prefs ->
            SettingsState(
                showWordCount =
                    prefs[SettingsKeys.SHOW_WORD_COUNT] ?: true,
                autosaveEnabled =
                    prefs[SettingsKeys.AUTOSAVE_ENABLED] ?: true
            )
        }

    suspend fun setShowWordCount(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.SHOW_WORD_COUNT] = enabled
        }
    }

    suspend fun setAutosaveEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.AUTOSAVE_ENABLED] = enabled
        }
    }
}
