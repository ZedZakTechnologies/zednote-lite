package com.zedzak.zednotelite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zedzak.zednotelite.ui.viewmodel.NotesViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.zedzak.zednotelite.data.NotesDataSource

import com.zedzak.zednotelite.data.local.DatabaseProvider
import com.zedzak.zednotelite.data.local.RoomNotesDataSource
import com.zedzak.zednotelite.data.local.RoomNotesRepository
import androidx.compose.runtime.remember
import com.zedzak.zednotelite.data.settings.SettingsRepository
import com.zedzak.zednotelite.data.settings.settingsDataStore
import com.zedzak.zednotelite.ui.settings.SettingsViewModel
import com.zedzak.zednotelite.ui.settings.SettingsScreen

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZedNoteLiteTheme {
                AppRoot()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val notesViewModel: NotesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = DatabaseProvider
                    .getDatabase(context)
                    .noteDao()

                @Suppress("UNCHECKED_CAST")
                return NotesViewModel(
                    repository = RoomNotesRepository(dao)
                ) as T

            }
        }
    )

    val settingsRepository = remember {
        SettingsRepository(context.applicationContext.settingsDataStore)
    }

    val settingsViewModel = remember {
        SettingsViewModel(settingsRepository)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.HOME) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("ZedNote Lite") },
                            actions = {
                                IconButton(
                                    onClick = { navController.navigate("settings") }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    HomeScreen(
                        viewModel = notesViewModel,
                        onOpenEditor = { /* existing logic */ },
                        onOpenNote = { noteId ->
                            navController.navigate(Routes.editor(noteId))
                        },
                        onOpenSettings = {
                            navController.navigate("settings")
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }


            composable(
                route = Routes.EDITOR,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: return@composable

                EditorScreen(
                    viewModel = notesViewModel,
                    noteId = noteId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }



        }
    }
}

