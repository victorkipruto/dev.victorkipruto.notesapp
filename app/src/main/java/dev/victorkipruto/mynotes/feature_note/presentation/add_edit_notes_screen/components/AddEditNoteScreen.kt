package dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.AddEditNoteEvent
import dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.AddEditNoteViewModel
import dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen.NoteTextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen (
    navController: NavController,
    noteColor:Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
)
{
    val titleState:NoteTextFieldState = viewModel.noteTitle.value
    val contentState:NoteTextFieldState = viewModel.noteContent.value

    val scaffoldState:SnackbarHostState = remember{SnackbarHostState()}
    val scope:CoroutineScope = rememberCoroutineScope()

    val noteBackgroundAnimatable = remember{
        Animatable(Color(if(noteColor != -1) noteColor else viewModel.noteColor.value))
    }
    
    //Observe UI Events from ViewModel
    LaunchedEffect(key1 = true)
    {
        viewModel.eventFlow.collectLatest { event->
            when(event)
            {
                is AddEditNoteViewModel.UiEvents.ShowSnackBar -> {
                    scaffoldState.showSnackbar(message = event.message)
                }

                AddEditNoteViewModel.UiEvents.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }
    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                containerColor = MaterialTheme.colorScheme.primary
            )
            {
               Icon(imageVector = Icons.Default.Done, contentDescription ="Save note")
            }
        },
       snackbarHost = {SnackbarHost(hostState = scaffoldState)}

    )
    {padding->
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(padding)
                .background(noteBackgroundAnimatable.value)

        )
        {
            LazyRow(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                item {
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(12.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = when (viewModel.noteColor.value == colorInt) {
                                        true -> Color.Black
                                        false -> Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBackgroundAnimatable.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(durationMillis = 400)
                                        )
                                    }
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TransparentHintTextField(
                hint = titleState.hint,
                text = titleState.text,
                onValueChange = {title ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(title))
                },
                onFocusChange = {titleFocus->
                    viewModel.onEvent(AddEditNoteEvent.ChangedTitleFocus(titleFocus))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                hint = contentState.hint,
                text = contentState.text,
                onValueChange = {content ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(content))
                },
                onFocusChange = {contentFocus->
                    viewModel.onEvent(AddEditNoteEvent.ChangedContentFocus(contentFocus))
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }

}