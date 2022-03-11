package com.manicpixie.annoteappcompose.domain.use_case


import com.manicpixie.annoteappcompose.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}