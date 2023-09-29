package dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.components

import android.text.Layout.Alignment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.NotesEvents
import dev.victorkipruto.mynotes.feature_note.presentation.notes_screen.NotesViewModel
import dev.victorkipruto.mynotes.feature_note.presentation.util.Screen
import dev.victorkipruto.mynotes.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController : NavController,
    viewModel:NotesViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value
    val scaffoldState = remember{ SnackbarHostState()}

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {},
        bottomBar = {},
        snackbarHost = {SnackbarHost(hostState = scaffoldState)},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNotesScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            )
            {
                Icon(imageVector = Icons.Default.List, contentDescription = "New note")
            }
        }
    )
    {padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            )
            {

                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvents.ToggleOrderSection)
                    }
                )
                {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            )
            {
                OrderSection(
                    modifier = Modifier.fillMaxWidth(),
                    noteOrder = state.noteOrder,
                    onOrderChange ={
                        viewModel.onEvent(NotesEvents.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
            )
            {
                items(state.notes)
                {note->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditNotesScreen.route +
                                            "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvents.DeleteNote(note))
                            scope.launch {
                                val results = scaffoldState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if(results == SnackbarResult.ActionPerformed)
                                {
                                    viewModel.onEvent(NotesEvents.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

        }
    }

}