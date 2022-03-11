package com.manicpixie.annoteappcompose.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote,
    val checkIfNoteExists: CheckIfNoteExists,
    val getNoteByDate: GetNoteByDate
)