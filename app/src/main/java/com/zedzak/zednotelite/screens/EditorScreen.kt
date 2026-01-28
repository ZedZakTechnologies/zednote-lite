package com.zedzak.zednotelite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.util.UUID
import com.zedzak.zednotelite.data.NotesRepository
import com.zedzak.zednotelite.model.Note
import com.zedzak.zednotelite.state.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val note = AppState.currentNote

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        saveNote(title, content)
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("Write your note…") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}


private fun saveNote(title: String, content: String) {
    val now = System.currentTimeMillis()

    val note = AppState.currentNote
    if (note == null) {
        NotesRepository.addNote(
            Note(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                lastUpdated = now
            )
        )
    } else {
        NotesRepository.updateNote(
            note.copy(
                title = title,
                content = content,
                lastUpdated = now
            )
        )
    }

    AppState.clearCurrentNote()
}
