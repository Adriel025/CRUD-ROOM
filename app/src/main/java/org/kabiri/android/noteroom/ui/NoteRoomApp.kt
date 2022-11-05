package org.kabiri.android.noteroom.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.kabiri.android.noteroom.ui.home.HomeScreen
import org.kabiri.android.noteroom.ui.note.NoteScreen
import org.kabiri.android.noteroom.ui.theme.NoteRoomTheme
import org.kabiri.android.noteroom.viewmodel.HomeViewModelAbstract

enum class Screen {
    Home, Note
}

@Composable
fun NoteRoomApp(
    homeViewModel: HomeViewModelAbstract,
) {
    val navController = rememberNavController()
    NoteRoomTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.name,
                builder = {
                    composable(Screen.Home.name) {
                        HomeScreen(
                            homeViewModel = homeViewModel,
                            onClickNote = {
                                navController.navigate(Screen.Note.name)
                            },
                            onClickAddNote = {
                                navController.navigate(Screen.Note.name)
                            }
                        )
                    }
                    composable(Screen.Note.name) {
                        NoteScreen(
                            viewModel = homeViewModel,
                            onClickClose = {
                                navController.popBackStack()
                            },
                        )
                    }
                })
        }
    }
}