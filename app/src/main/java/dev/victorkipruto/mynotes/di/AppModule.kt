package dev.victorkipruto.mynotes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.victorkipruto.mynotes.feature_note.data.data_source.NoteDatabase
import dev.victorkipruto.mynotes.feature_note.data.repository.NoteRepositoryImpl
import dev.victorkipruto.mynotes.feature_note.domain.repository.NoteRepository
import dev.victorkipruto.mynotes.feature_note.domain.use_case.AddNote
import dev.victorkipruto.mynotes.feature_note.domain.use_case.DeleteNote
import dev.victorkipruto.mynotes.feature_note.domain.use_case.GetNote
import dev.victorkipruto.mynotes.feature_note.domain.use_case.GetNotes
import dev.victorkipruto.mynotes.feature_note.domain.use_case.NotesUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application):NoteDatabase
    {
        return Room.databaseBuilder(app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db:NoteDatabase): NoteRepository
    {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases
    {
        return NotesUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}