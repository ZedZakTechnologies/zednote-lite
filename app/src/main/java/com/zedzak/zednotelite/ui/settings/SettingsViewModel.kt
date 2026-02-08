package com.zedzak.zednotelite.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedzak.zednotelite.data.settings.SettingsRepository
import com.zedzak.zednotelite.data.settings.SettingsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.zedzak.zednotelite.model.NoteSortMode
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<SettingsState> =
        repository.settingsFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsState()
            )

    val sortMode: StateFlow<NoteSortMode> =
        settings
            .map { it.sortMode }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NoteSortMode.LAST_EDITED
            )

    fun setShowWordCount(enabled: Boolean) {
        viewModelScope.launch {
            repository.setShowWordCount(enabled)
        }
    }

    fun updateSortMode(mode: NoteSortMode) {
        viewModelScope.launch {
            repository.updateSortMode(mode)
        }
    }

    fun setAutosaveEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAutosaveEnabled(enabled)
        }
    }
}
