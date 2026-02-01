package com.zedzak.zednotelite.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zedzak.zednotelite.model.Note
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.zedzak.zednotelite.ui.viewmodel.NotesViewModel




@Composable
fun HomeScreen(
    viewModel: NotesViewModel,
    onOpenEditor: () -> Unit,
    onOpenNote: (String) -> Unit,
    modifier: Modifier = Modifier
)  {
    val notes by viewModel.notes.collectAsState()
    val visibleNotes = notes.filter {
        it.title.isNotBlank() || it.content.isNotBlank()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ZedNote Lite",
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = onOpenEditor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("New Note")
        }

        if (notes.isEmpty()) {
            Text(
                text = "No notes yet",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes) { note ->
                    NoteRow(
                        note = note,
                        onClick = { onOpenNote(note.id) }
                    )
                }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ZedNote Lite",
            style = MaterialTheme.typography.titleLarge
        )

        Button(onClick = onOpenEditor) {
            Text("New Note")
        }





        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteRow(
                    note = note,
                    onClick = { onOpenNote(note.id) }
                )
            }
        }

    }
}


@Composable
fun NoteRow(
    note: Note,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content.take(80),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
