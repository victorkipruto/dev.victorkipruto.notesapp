package dev.victorkipruto.mynotes.feature_note.domain.use_case

import dev.victorkipruto.mynotes.feature_note.domain.model.InvalidNoteException
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import dev.victorkipruto.mynotes.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(private val repository: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note:Note){
        if(note.title.isBlank())
        {
            throw InvalidNoteException(BLANK_TITLE_ERR_MSG)
        }

        if(note.content.isBlank())
        {
            throw InvalidNoteException(BLANK_CONTENT_ERR_MSG)
        }
        repository.insertNote(note)
    }
    companion object{
        val BLANK_TITLE_ERR_MSG="Title Cannot be Empty"
        val BLANK_CONTENT_ERR_MSG="Content cannot be Empty"
    }
}