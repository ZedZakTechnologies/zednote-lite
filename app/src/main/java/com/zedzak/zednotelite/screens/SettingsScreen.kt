package com.zedzak.zednotelite.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onOpenEditor: () -> Unit = {}
) {
    Text(
        text = "Settings Screen",
        modifier = modifier
    )
}