package com.example.digitaldiary.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.digitaldiary.R
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.domain.ValidateTitleUseCase
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.state.AddNewNoteState
import com.example.digitaldiary.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository, private val validateTitleUseCase: ValidateTitleUseCase
) : ViewModel() {

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

                if (!validateTitleUseCase(state.title)) {
                    state = state.copy(titleError = UiText.StringResource(R.string.title_cannot_be_empty))
                    return
                }

                val note = mapOf(
                    "title" to state.title,
                    "content" to state.content,
                    "isPhotoAttached" to false,
                    "isAudioAttached" to false
                )

                noteRepository.addNote(note).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        state = state.copy(success = true)
                    } else {
                        Log.e("MainActivity", "Failed to add note.", task.exception)
                    }
                }

                state = AddNewNoteState()
            }

            is AddNewNoteFormEvent.Cancel -> {
                state = AddNewNoteState()
            }
        }
    }
}

