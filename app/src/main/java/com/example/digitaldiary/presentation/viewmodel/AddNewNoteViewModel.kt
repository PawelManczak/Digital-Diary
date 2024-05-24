package com.example.digitaldiary.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.state.AddNewNoteState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

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

                val note = mapOf(
                    "title" to state.title,
                    "content" to state.content
                )

                noteRepository.addNote(note)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MainActivity", "Note added successfully.")
                        } else {
                            Log.e("MainActivity", "Failed to add note.", task.exception)
                        }
                    }
            }

            is AddNewNoteFormEvent.Cancel -> {
                // Cancel the note
            }
        }
    }
}

