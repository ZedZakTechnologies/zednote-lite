package com.zedzak.zednotelite.data.settings

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_DATASTORE_NAME = "settings"

val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATASTORE_NAME
)
