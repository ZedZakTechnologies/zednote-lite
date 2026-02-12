package com.zedzak.zednotelite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import com.zedzak.zednotelite.ui.viewmodel.NotesViewModel
import com.zedzak.zednotelite.model.Note
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.imePadding


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign
import com.zedzak.zednotelite.ui.settings.SettingsViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel,
    settingsViewModel: SettingsViewModel,
    noteId: Long,
    onBack: () -> Unit
) {
    val activeNote by viewModel.activeNote.collectAsState()
    val showWordCount by settingsViewModel.showWordCount.collectAsState()
    val autosaveEnabled by settingsViewModel.autosaveEnabled.collectAsState()


    // 1) Load the requested note whenever noteId changes
    LaunchedEffect(noteId) {
        viewModel.openNote(noteId)
    }

    // 2) Local editor fields (don’t initialize from activeNote here)
    var title by rememberSaveable(noteId) { mutableStateOf("") }
    var content by rememberSaveable(noteId) { mutableStateOf("") }

    // 3) When the RIGHT note arrives, hydrate the UI fields
    LaunchedEffect(activeNote?.id, noteId) {
        val n = activeNote
        if (n != null && n.id == noteId) {
            title = n.title
            content = n.content
        }
    }

    // Word count (simple + reliable)
    val wordCount by remember(content) {
        derivedStateOf {
            content.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveAndClose(title, content)
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle

                    if (autosaveEnabled) {
                        // Only autosave if we are editing the correct loaded note
                        val n = activeNote
                        if (n != null && n.id == noteId) {
                            viewModel.onEditorChanged(n.copy(title = newTitle, content = content))
                        }
                    }

                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = content,
                onValueChange = { newContent ->
                    content = newContent

                    if (autosaveEnabled) {
                        val n = activeNote
                        if (n != null && n.id == noteId) {
                            viewModel.onEditorChanged(n.copy(title = title, content = newContent))
                        }
                    }
                },
                label = { Text("Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            if (showWordCount) {
                Text(
                    text = "Words: $wordCount",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}

