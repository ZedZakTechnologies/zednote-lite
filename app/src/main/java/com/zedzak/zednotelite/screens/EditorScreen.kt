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
import com.zedzak.zednotelite.ui.viewmodel.NotesViewModel
import com.zedzak.zednotelite.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel,
    onBack: () -> Unit
) {
    val activeNote by viewModel.activeNote.collectAsState()

    val title = activeNote?.title.orEmpty()
    val content = activeNote?.content.orEmpty()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveNote(title, content)
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                onValueChange = { newTitle ->
                    activeNote?.let { note ->
                        viewModel.onEditorChanged(
                            note.copy(title = newTitle)
                        )
                    }
                },
                placeholder = { Text("Title") }
            )


            OutlinedTextField(
                value = content,
                onValueChange = { newContent ->
                    activeNote?.let { note ->
                        viewModel.onEditorChanged(
                            note.copy(content = newContent)
                        )
                    }
                },
                placeholder = { Text("Write your note…") }
            )



        }
    }
}



