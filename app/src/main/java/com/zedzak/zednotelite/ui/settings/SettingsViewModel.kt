package com.zedzak.zednotelite.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedzak.zednotelite.data.settings.SettingsRepository
import com.zedzak.zednotelite.data.settings.SettingsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun setShowWordCount(enabled: Boolean) {
        viewModelScope.launch {
            repository.setShowWordCount(enabled)
        }
    }

    fun setAutosaveEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAutosaveEnabled(enabled)
        }
    }
}
