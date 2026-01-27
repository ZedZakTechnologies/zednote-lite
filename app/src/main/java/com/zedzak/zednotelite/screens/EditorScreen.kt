package com.zedzak.zednotelite.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Text(
        text = "Editor Screen",
        modifier = modifier
    )
}
