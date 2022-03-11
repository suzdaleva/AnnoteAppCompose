package com.manicpixie.annoteappcompose.presentation.util

sealed class Screen(val route: String) {
    object CalendarScreen : Screen("calendar_screen")
    object SpinnerScreen : Screen("spinner_screen")
    object NoteScreen : Screen("note_screen")
}
