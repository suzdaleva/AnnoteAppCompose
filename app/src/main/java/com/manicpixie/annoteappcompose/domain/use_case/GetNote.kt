package com.manicpixie.annoteappcompose.domain.use_case

import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note {
        return repository.getNoteById(id)
    }
}