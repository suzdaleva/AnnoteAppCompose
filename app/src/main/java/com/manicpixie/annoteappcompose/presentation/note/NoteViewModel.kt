package com.manicpixie.annoteappcompose.presentation.note

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.manicpixie.annoteappcompose.domain.model.Note
import com.manicpixie.annoteappcompose.domain.use_case.NoteUseCases
import com.manicpixie.annoteappcompose.presentation.util.setMidnight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@SuppressLint("SimpleDateFormat")
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteContent = mutableStateOf("")
    val noteContent: State<String> = _noteContent

    private val _isSaveButtonPressed = mutableStateOf(false)
    val isSaveButtonPressed: State<Boolean> = _isSaveButtonPressed

    private val _isDeleteButtonPressed = mutableStateOf(false)
    val isDeleteButtonPressed: State<Boolean> = _isDeleteButtonPressed

    private var currentNoteId: Int? = null

    private val _currentDate = mutableStateOf(Calendar.getInstance())
    val currentDate: State<Calendar> = _currentDate

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
    }


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId).also { note ->
                        currentNoteId = note.id
                        _noteContent.value = note.note
                    }
                }
            }
        }
        savedStateHandle.get<String>("noteDate")?.let {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val date: Date = sdf.parse(it) ?: return@let
            val calendar = Calendar.getInstance()
            calendar.time = date
            _currentDate.value = calendar.setMidnight()
        }

    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.EnteredContent -> {
                _noteContent.value = event.value
            }
            is NoteEvent.SaveNote -> {
                _isSaveButtonPressed.value = true
                saveNote()
            }
            is NoteEvent.DeleteNote -> {
                _isDeleteButtonPressed.value = true
                deleteNote()
            }

        }
    }


    private fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        note = noteContent.value,
                        date = currentDate.value,
                        modify_date = Calendar.getInstance(),
                        id = currentNoteId
                    )
                )
            } catch (e: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "This note is empty"
                    )
                )
            }
        }
    }


    private fun deleteNote() {
        if (currentNoteId != null) {
            Log.i("note", "back pressed $currentNoteId")
            viewModelScope.launch {
                noteUseCases.deleteNote(currentNoteId!!)
            }
        }
    }


    fun onBackPressed() {
        if (noteContent.value.isNotEmpty()) {
            viewModelScope.launch {
                noteUseCases.addNote(
                    Note(
                        note = noteContent.value,
                        date = currentDate.value,
                        modify_date = Calendar.getInstance(),
                        id = currentNoteId
                    )
                )
            }
        } else {
            deleteNote()
        }
    }
}


