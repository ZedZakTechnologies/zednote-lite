package com.zedzak.zednotelite.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenEditor: () -> Unit = {}
) {
    Text(
        text = "Home Screen",
        modifier = modifier
    )
}

