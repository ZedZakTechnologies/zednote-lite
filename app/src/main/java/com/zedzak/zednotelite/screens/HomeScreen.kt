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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import com.zedzak.zednotelite.model.NoteSortMode
import com.zedzak.zednotelite.ui.settings.SettingsViewModel
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.zedzak.zednotelite.model.SortDirection


@Composable
fun HomeScreen(
    viewModel: NotesViewModel,
    settingsViewModel: SettingsViewModel,
    onOpenEditor: () -> Unit,
    onOpenNote: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
)  {
    val notes by viewModel.visibleNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortMode by settingsViewModel.sortMode.collectAsState()
    val sortDirection by settingsViewModel.sortDirection.collectAsState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Text(
        //    text = "ZedNote Lite",
        //    style = MaterialTheme.typography.titleLarge
       // )

        TextField(
            value = searchQuery,
            onValueChange = { text ->
                viewModel.updateSearchQuery(text)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search notes") },
            singleLine = true
        )

        var sortMenuExpanded by remember { mutableStateOf(false) }


        Box {
            IconButton(onClick = { sortMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Sort notes"
                )
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            sortLabel(
                                label = "Last edited",
                                isActive = sortMode == NoteSortMode.LAST_EDITED,
                                direction = sortDirection
                            )
                        )
                    },
                    onClick = {
                        settingsViewModel.onSortModeSelected(NoteSortMode.LAST_EDITED)
                        sortMenuExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            sortLabel(
                                label = "Created date",
                                isActive = sortMode == NoteSortMode.CREATED_DATE,
                                direction = sortDirection
                            )
                        )
                    },
                    onClick = {
                        settingsViewModel.onSortModeSelected(NoteSortMode.CREATED_DATE)
                        sortMenuExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            sortLabel(
                                label = "Title",
                                isActive = sortMode == NoteSortMode.TITLE,
                                direction = sortDirection
                            )
                        )
                    },
                    onClick = {
                        settingsViewModel.onSortModeSelected(NoteSortMode.TITLE)
                        sortMenuExpanded = false
                    }
                )

            }
        }
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
                        onClick = { onOpenNote(note.id) },
                        onTogglePin = {
                            viewModel.togglePin(note.id)
                        }
                    )
                }
            }
        }
    }



}

private fun sortLabel(
    label: String,
    isActive: Boolean,
    direction: SortDirection
): String {
    return if (!isActive) label
    else "$label ${if (direction == SortDirection.ASC) "▲" else "▼"}"
}

@Composable
fun NoteRow(
    note: Note,
    onClick: () -> Unit,
    onTogglePin: () -> Unit
) {
    val cardColor =
        if (note.isPinned)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else
            MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                //Text(
                //    text = note.content.take(80),
                //    style = MaterialTheme.typography.bodyMedium
                //)
                Text(
                    text = note.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    onTogglePin()
                }
            ) {
                Icon(
                    imageVector =
                        if (note.isPinned) Icons.Filled.PushPin
                        else Icons.Outlined.PushPin,
                    contentDescription = "Pin note"
                )
            }

        }

    }
}
