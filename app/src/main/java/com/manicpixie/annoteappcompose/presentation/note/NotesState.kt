package com.manicpixie.annoteappcompose.presentation.note

import com.manicpixie.annoteappcompose.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList()
)
