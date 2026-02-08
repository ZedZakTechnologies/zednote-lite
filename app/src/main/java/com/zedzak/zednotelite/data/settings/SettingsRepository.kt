package com.zedzak.zednotelite.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.zedzak.zednotelite.model.NoteSortMode
import com.zedzak.zednotelite.model.SortDirection

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    val settingsFlow: Flow<SettingsState> =
        dataStore.data.map { prefs ->
            SettingsState(
                showWordCount =
                    prefs[SettingsKeys.SHOW_WORD_COUNT] ?: true,
                autosaveEnabled =
                    prefs[SettingsKeys.AUTOSAVE_ENABLED] ?: true,

                sortMode =
                    prefs[SettingsKeys.SORT_MODE]
                        ?.let { NoteSortMode.valueOf(it) }
                    ?: NoteSortMode.LAST_EDITED,

                sortDirection =
                    prefs[SettingsKeys.SORT_DIRECTION]
                        ?.let { SortDirection.valueOf(it) }
                        ?: SortDirection.DESC

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

    suspend fun updateSortMode(mode: NoteSortMode) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.SORT_MODE] = mode.name
        }
    }

    suspend fun updateSortDirection(direction: SortDirection) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.SORT_DIRECTION] = direction.name
        }
    }


}
