package com.zedzak.zednotelite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingToggle(
                title = "Dark Mode",
                description = "Enable dark theme (coming soon)"
            )

            SettingToggle(
                title = "Lock App",
                description = "Require authentication to open app"
            )

            Text(
                text = "More settings coming soon",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SettingToggle(
    title: String,
    description: String
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = description, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Switch(
            checked = false,
            onCheckedChange = null, // intentionally disabled
            enabled = false
        )
    }
}
