package com.manicpixie.annoteappcompose.presentation.note


sealed class NoteEvent {
    object SaveNote : NoteEvent()
    object DeleteNote : NoteEvent()
    data class EnteredContent(val value: String) : NoteEvent()
}