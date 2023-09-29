package dev.victorkipruto.mynotes.feature_note.presentation.notes_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.domain.use_case.NotesUseCases
import dev.victorkipruto.mynotes.feature_note.domain.util.NoteOrder
import dev.victorkipruto.mynotes.feature_note.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(val useCases: NotesUseCases):ViewModel()
{
    private val _state = mutableStateOf<NotesStates>(NotesStates())
    val state: State<NotesStates> = _state
    var getNotesJob: Job? = null
    var deletedNote: Note? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(events: NotesEvents)
    {
        when(events)
        {
            is NotesEvents.DeleteNote -> {
                viewModelScope.launch {
                    useCases.deleteNote(events.note)
                    deletedNote = events.note
                }
            }

            is NotesEvents.Order -> {
                if (
                    state.value.noteOrder::class == events.noteOrder::class &&
                    state.value.noteOrder.orderType::class == events.noteOrder.orderType::class
                    )
                {
                    return
                }
                getNotes(events.noteOrder)
            }

            is NotesEvents.RestoreNote -> {
                viewModelScope.launch {
                    useCases.addNote(deletedNote ?: return@launch)
                    deletedNote = null
                }
            }

            is NotesEvents.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotes(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}