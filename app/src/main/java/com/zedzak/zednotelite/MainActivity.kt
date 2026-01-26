package com.zedzak.zednotelite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zedzak.zednotelite.ui.theme.ZedNoteLiteTheme
import com.zedzak.zednotelite.screens.HomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zedzak.zednotelite.navigation.Routes
import com.zedzak.zednotelite.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZedNoteLiteTheme {
                AppRoot()
            }
        }

    }
}


@Composable
fun AppRoot() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.EDITOR) { EditorScreen() }
            composable(Routes.SEARCH) { SearchScreen() }
            composable(Routes.SETTINGS) { SettingsScreen() }
            composable(Routes.SECURITY) { SecurityGateScreen() }
        }
    }
}
