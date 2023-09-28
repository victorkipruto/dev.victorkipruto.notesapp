package dev.victorkipruto.mynotes.feature_note.presentation.notes_screen

import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.domain.util.NoteOrder
import dev.victorkipruto.mynotes.feature_note.domain.util.OrderType

data class NotesStates(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible:Boolean = false
)