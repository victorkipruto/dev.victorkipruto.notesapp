package dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.domain.use_case.NotesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class AddEditNoteViewModel @Inject constructor(private val notesUseCases: NotesUseCases)
    : ViewModel()
{
        private val _noteTitle:MutableState<NoteTextFieldState> = mutableStateOf(
            NoteTextFieldState()
        )
        val  noteTitle: State<NoteTextFieldState> = _noteTitle

        private val _noteContent:MutableState<NoteTextFieldState> = mutableStateOf(
            NoteTextFieldState()
        )
        val noteContent:State<NoteTextFieldState> = _noteContent

        private val _noteColor:MutableState<Int> = mutableStateOf(Note.noteColors.random().toArgb())
        val noteColor:State<Int> = _noteColor





}