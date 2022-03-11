package com.manicpixie.annoteappcompose.di

import android.app.Application
import androidx.room.Room
import com.manicpixie.annoteappcompose.data.local.NoteDatabase
import com.manicpixie.annoteappcompose.data.repository.NoteRepositoryImpl
import com.manicpixie.annoteappcompose.domain.repository.NoteRepository
import com.manicpixie.annoteappcompose.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository),
            checkIfNoteExists = CheckIfNoteExists(repository),
            getNoteByDate = GetNoteByDate(repository)
        )
    }
}