package dev.victorkipruto.mynotes.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.components.AddEditNoteScreen
import dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.components.NotesScreen
import dev.victorkipruto.mynotes.feature_note.presentation.util.Screen
import dev.victorkipruto.mynotes.ui.theme.MyNotesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val navController:NavHostController  = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                        )
                    {
                        composable(route = Screen.NotesScreen.route)
                        {
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screen.AddEditNotesScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteId")
                                {
                                    type= NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name="noteColor")
                                {
                                    type= NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        )
                        {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color)
                        }
                    }

                }
            }
        }
    }
}

