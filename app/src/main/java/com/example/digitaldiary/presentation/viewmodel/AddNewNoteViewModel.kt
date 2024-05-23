package com.example.digitaldiary.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.state.AddNewNoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(AddNewNoteState())

    fun onEvent(event: AddNewNoteFormEvent) {
        when (event) {
            is AddNewNoteFormEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }
            is AddNewNoteFormEvent.ContentChanged -> {
                state = state.copy(content = event.content)
            }
            is AddNewNoteFormEvent.PhotoAttached -> {
                // not implemented
            }
            is AddNewNoteFormEvent.AudioAttached -> {
                // not implemented
            }
            is AddNewNoteFormEvent.Submit -> {
                // Submit the note
            }
            is AddNewNoteFormEvent.Cancel -> {
                // Cancel the note
            }
        }
    }
}

