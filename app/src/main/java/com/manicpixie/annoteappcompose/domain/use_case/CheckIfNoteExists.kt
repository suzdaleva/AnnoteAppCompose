package com.manicpixie.annoteappcompose.domain.use_case

import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.repository.NoteRepository
import java.util.*

class CheckIfNoteExists(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(date: Calendar): Boolean {
        return repository.exists(date)
    }
}