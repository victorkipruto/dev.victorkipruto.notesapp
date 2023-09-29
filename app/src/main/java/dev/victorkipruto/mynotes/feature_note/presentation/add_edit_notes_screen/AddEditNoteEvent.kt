package dev.victorkipruto.mynotes.feature_note.presentation.add_edit_notes_screen

import androidx.compose.ui.focus.FocusState
import dev.victorkipruto.mynotes.feature_note.domain.model.Note

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value:String): AddEditNoteEvent()
    data class EnteredContent(val value:String): AddEditNoteEvent()
    data class ChangedTitleFocus(val focusState:FocusState): AddEditNoteEvent()
    data class ChangedContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    object SaveNote:AddEditNoteEvent()
}