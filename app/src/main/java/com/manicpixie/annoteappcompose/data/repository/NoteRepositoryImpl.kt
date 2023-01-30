package com.manicpixie.annoteappcompose.data.repository


import com.manicpixie.annoteappcompose.data.local.NoteDao
import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override suspend fun insert(note: Note) {
        dao.insert(note)
    }

    override suspend fun deleteNote(key: Int) {
        dao.deleteNote(key)
    }

    override suspend fun getNoteByDate(date: Calendar): Note {
        return dao.getNoteByDate(date)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes()
    }

    override suspend fun exists(date: Calendar): Boolean {
        return dao.exists(date)
    }

    override suspend fun getNoteById(key: Int): Note {
        return dao.getNoteById(key)
    }
}