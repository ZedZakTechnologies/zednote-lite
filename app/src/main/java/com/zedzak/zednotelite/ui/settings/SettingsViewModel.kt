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
import com.zedzak.zednotelite.model.SortDirection

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

    val sortDirection: StateFlow<SortDirection> =
        settings
            .map { it.sortDirection }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SortDirection.DESC
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

    fun onSortModeSelected(mode: NoteSortMode) {
        viewModelScope.launch {
            val current = settings.value

            if (current.sortMode == mode) {
                // Same mode tapped again → toggle direction
                val newDirection =
                    if (current.sortDirection == SortDirection.ASC)
                        SortDirection.DESC
                    else
                        SortDirection.ASC

                repository.updateSortDirection(newDirection)
            } else {
                // New mode selected → reset direction to ASC
                repository.updateSortMode(mode)
                repository.updateSortDirection(SortDirection.ASC)
            }
        }
    }


    fun updateSortDirection(direction: SortDirection) {
        viewModelScope.launch {
            repository.updateSortDirection(direction)
        }
    }

    fun setAutosaveEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAutosaveEnabled(enabled)
        }
    }
}
