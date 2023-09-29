package dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.victorkipruto.mynotes.feature_note.domain.model.InvalidNoteException
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.domain.use_case.NotesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle
    )
    : ViewModel()
{
    private val _noteTitle:MutableState<NoteTextFieldState> = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title"
        )
    )
    val  noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent:MutableState<NoteTextFieldState> = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter content"
        )
    )
    val noteContent:State<NoteTextFieldState> = _noteContent

    private val _noteColor:MutableState<Int> = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor:State<Int> = _noteColor

    private var currentNoteId:Int? = null
    private val _eventFlow: MutableSharedFlow<UiEvents> = MutableSharedFlow()
    val eventFlow:SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

    init {
        val noteId = savedStateHandle.get<Int>("noteId")?.let {note_id->
            if(note_id != -1)
            {
                viewModelScope.launch {
                    notesUseCases.getNote(note_id)?.also {note->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteColor.value = note.color

                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )

                    }
                }

            }
        }

    }
    sealed class UiEvents
    {
        data class ShowSnackBar(val message:String): UiEvents()
        object SaveNote:UiEvents()
    }

    fun onEvent(event: AddEditNoteEvent)
    {
        when(event)
        {
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color

            }

            is AddEditNoteEvent.ChangedContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangedTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text=event.value
                )
            }

            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                    notesUseCases.addNote(Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteColor.value,
                        id = currentNoteId
                    ))
                    _eventFlow.emit(UiEvents.SaveNote)
                    }catch (e:InvalidNoteException) {
                        _eventFlow.emit(UiEvents.ShowSnackBar(
                            message = e.message ?: "Could not save note "
                        ))
                    }
                }
            }
        }
    }





}