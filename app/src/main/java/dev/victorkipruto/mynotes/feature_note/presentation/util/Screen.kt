package dev.victorkipruto.mynotes.feature_note.presentation.util

sealed class Screen(val route:String)
{
    object NotesScreen: Screen("notes_screen")
    object AddEditNotesScreen:Screen("notes_add_edit")
}