package com.manicpixie.annoteappcompose.domain.use_case

import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(note: Note) {
        if (note.note.isBlank()) {
            throw Exception("The content of the note should not be empty.")
        }
        repository.insert(note)
    }
}