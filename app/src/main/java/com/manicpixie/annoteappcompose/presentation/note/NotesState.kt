package com.manicpixie.annoteappcompose.presentation.note


import com.manicpixie.annoteappcompose.domain.model.Note
import java.util.*

data class NotesState(
    val notes: List<Note> = emptyList(),
    val recentlyVisited : Calendar? = null
)
