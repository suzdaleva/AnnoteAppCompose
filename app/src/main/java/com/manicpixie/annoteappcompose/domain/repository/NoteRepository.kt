package com.manicpixie.annoteappcompose.domain.repository


import com.manicpixie.annoteappcompose.domain.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoteRepository {

    suspend fun insert(note: Note)

    suspend fun deleteNote(key: Int)

    suspend fun getNoteByDate(date: Calendar): Note

    fun getAllNotes(): Flow<List<Note>>

    suspend fun exists(date: Calendar): Boolean

    suspend fun getNoteById(key: Int): Note
}