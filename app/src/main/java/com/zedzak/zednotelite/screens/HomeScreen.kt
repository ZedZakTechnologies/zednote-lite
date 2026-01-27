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



@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenEditor: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenSettingGate: () -> Unit,
) {
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

        Button(onClick = onOpenSearch) {
            Text("Search Notes")
        }

        Button(onClick = onOpenSettings) {
            Text("Settings")
        }

        Button(onClick = onOpenSettingGate) {
            Text("Security Gate")
        }



    }
}


