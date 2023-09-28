package dev.victorkipruto.mynotes.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.victorkipruto.mynotes.feature_note.data.repository.FakeNoteRepository
import dev.victorkipruto.mynotes.feature_note.domain.model.InvalidNoteException
import dev.victorkipruto.mynotes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class AddNoteTest{


    private lateinit var repository: FakeNoteRepository
    private lateinit var addNote: AddNote

    @Before
    fun setUp()
    {
        repository = FakeNoteRepository()
        addNote = AddNote(repository)
    }

    @Test
    fun `Throws Exception when title is Blank`(){
        val emptyTitleNote:Note = Note(
            title = "",
            content = "This is a content",
            timestamp = 12L,
            color=123
        )
        val exception: Exception = assertThrows(InvalidNoteException::class.java){
            runBlocking {
                addNote(emptyTitleNote)
            }
        }

        assertThat(exception.message).matches(AddNote.BLANK_TITLE_ERR_MSG)

    }

    @Test
    fun `Throws Exception when Content is Blank`(){
        val emptyContentNote:Note = Note(
            title = "Title",
            content = "",
            timestamp = 12L,
            color=123
        )
        val exception: Exception = assertThrows(InvalidNoteException::class.java){
            runBlocking {
                addNote(emptyContentNote)
            }
        }
        assertThat(exception.message).matches(AddNote.BLANK_CONTENT_ERR_MSG)
    }


    @Test
    fun `Note is inserted when both content and title are not blank`() = runBlocking{
        val goodNote:Note = Note(
            title = "Title",
            content = "This is a content",
            timestamp = 12L,
            color=123
        )
        repository.insertNote(goodNote);
        val notes = repository.getNotes().first().size
        assertThat(notes).isEqualTo(1)

    }
}