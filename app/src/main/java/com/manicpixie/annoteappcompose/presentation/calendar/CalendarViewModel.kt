package com.manicpixie.annoteappcompose.presentation.calendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.use_case.NoteUseCases
import com.manicpixie.annoteappcompose.presentation.note.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _noteList = mutableStateOf(NotesState())
    val noteList: State<NotesState> = _noteList

    private var _currentPage = mutableStateOf(0)
    val currentPage: State<Int> = _currentPage


    init {
        getNotes()
        savedStateHandle.get<Int>("currentPage")?.let { currentPage ->
            if (currentPage != 0) {
                viewModelScope.launch {
                    _currentPage.value = currentPage
                }
            }
        }
    }

    suspend fun checkNote(date: Calendar): Boolean {
        var noteExists = false
        val job = viewModelScope.launch {
            noteExists = noteUseCases.checkIfNoteExists(date)
        }
        job.join()
        return noteExists
    }

    suspend fun getNoteByDate(date: Calendar): Note? {
        var note: Note? = null
        val job = viewModelScope.launch {
            note = noteUseCases.getNoteByDate(date)
        }
        job.join()
        return note
    }


    private fun getNotes() {
        noteUseCases.getNotes().onEach {
            _noteList.value = NotesState(
                notes = it,
                recentlyVisited = it.lastOrNull { note ->
                    note.modify_date.timeInMillis > Calendar.getInstance().timeInMillis - 1000
                }?.date
            )
        }.launchIn(viewModelScope)
    }

}