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
                    repository = RoomNotesDataSource(dao)
                ) as T
            }
        }
    )



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
                        notesViewModel.startNewNote { id ->
                            navController.navigate(Routes.editor(id))
                        }
                    },
                    onOpenNote = { noteId ->
                        navController.navigate(Routes.editor(noteId))
                    }
                )
            }

            composable(
                route = Routes.EDITOR,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.StringType }
                )
            ) {
                EditorScreen(
                    viewModel = notesViewModel,
                    onBack = { navController.popBackStack() }
                )
            }


        }
    }
}

