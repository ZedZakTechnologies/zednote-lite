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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zedzak.zednotelite.viewmodel.NotesViewModel


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
    val notesViewModel: NotesViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = notesViewModel,
                    onOpenEditor = {
                        notesViewModel.createNewNote()
                        navController.navigate(Routes.EDITOR)
                    },
                    onOpenSearch = { navController.navigate(Routes.SEARCH) },
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                    onOpenSettingGate = { navController.navigate(Routes.SECURITY) },
                    onOpenNote = { noteId ->
                        notesViewModel.openNote(noteId)
                        navController.navigate(Routes.editorWithId(noteId))
                    }
                )
            }




            composable(Routes.EDITOR) {
                EditorScreen(
                    viewModel = notesViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.EDITOR_WITH_ID,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                noteId?.let { notesViewModel.openNote(it) }

                EditorScreen(
                    viewModel = notesViewModel,
                    onBack = { navController.popBackStack() }
                )
            }



            composable(Routes.SEARCH) {
                SearchScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.SECURITY) {
                SecurityGateScreen(onBack = { navController.popBackStack() })
            }

        }
    }
}

