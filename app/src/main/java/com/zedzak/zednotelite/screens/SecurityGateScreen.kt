package com.zedzak.zednotelite.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SecurityGateScreen(
    modifier: Modifier = Modifier,
    onOpenEditor: () -> Unit = {}
) {
    Text(
        text = "SecurityGate Screen",
        modifier = modifier
    )
}