package com.zedzak.zednotelite.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedzak.zednotelite.data.settings.SettingsRepository
import com.zedzak.zednotelite.data.settings.SettingsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.zedzak.zednotelite.model.NoteSortMode
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged
import com.zedzak.zednotelite.model.SortDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


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

    private val sortDirectionsByMode =
        MutableStateFlow<Map<NoteSortMode, SortDirection>>(emptyMap())

    init {
        viewModelScope.launch {
            repository.sortDirectionsFlow()
                .collect { persisted ->
                    sortDirectionsByMode.update { current ->
                        // hydrate once only
                        if (current.isEmpty()) persisted else current
                    }
                }
        }
    }


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
        combine(
            sortMode,
            sortDirectionsByMode
        ) { mode, map ->
            map[mode] ?: SortDirection.ASC
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SortDirection.ASC
            )



    val showWordCount: StateFlow<Boolean> =
        settings
            .map { it.showWordCount }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    val autosaveEnabled: StateFlow<Boolean> =
        settings
            .map { it.autosaveEnabled }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = true
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
            val currentMode = sortMode.value
            val currentMap = sortDirectionsByMode.value.toMutableMap()

            if (currentMode == mode) {
                val currentDir =
                    currentMap[mode] ?: SortDirection.ASC

                val newDir =
                    if (currentDir == SortDirection.ASC)
                        SortDirection.DESC
                    else
                        SortDirection.ASC

                currentMap[mode] = newDir
                repository.persistSortDirection(mode, newDir)
            } else {
                currentMap.putIfAbsent(mode, SortDirection.ASC)
                repository.updateSortMode(mode)

                repository.persistSortDirection(
                    mode,
                    currentMap[mode]!!
                )
            }

            sortDirectionsByMode.value = currentMap
        }
    }


    fun setAutosaveEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAutosaveEnabled(enabled)
        }
    }
}
