package com.example.digitaldiary.presentation.viewmodel

import com.example.digitaldiary.R
import kotlinx.coroutines.tasks.await


import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.domain.ValidateTitleUseCase
import com.example.digitaldiary.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteRepository,
    private val validateTitleUseCase: ValidateTitleUseCase
) : ViewModel() {

    var state by mutableStateOf(EditNoteState())

    fun loadNoteDetails(noteId: String) {
        viewModelScope.launch {
            try {
                val note = noteRepository.getNoteById(noteId).await()
                val photoUrl = noteRepository.getPhotoUrl(noteId).await()
                val audioUrl = noteRepository.getAudioUrl(noteId).await()
                state = state.copy(
                    noteId = noteId,
                    title = note?.title ?: "",
                    content = note?.content ?: "",
                    photoUri = photoUrl,
                    audioUri = audioUrl
                )
            } catch (e: Exception) {
                Log.e("EditNoteViewModel", "Failed to load note details", e)
            }
        }
    }

    fun onEvent(event: EditNoteFormEvent) {
        when (event) {
            is EditNoteFormEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }

            is EditNoteFormEvent.ContentChanged -> {
                state = state.copy(content = event.content)
            }

            is EditNoteFormEvent.PhotoAttached -> {
                state = state.copy(photoUri = event.uri)
            }

            is EditNoteFormEvent.AudioAttached -> {
                state = state.copy(audioUri = event.uri)
            }

            is EditNoteFormEvent.Submit -> {

                if (!validateTitleUseCase(state.title)) {
                    state =
                        state.copy(titleError = UiText.StringResource(R.string.title_cannot_be_empty))
                    return
                }

                val cachedPhotoUri = state.photoUri
                val cachedAudioUri = state.audioUri

                viewModelScope.launch {

                    val note = mapOf(
                        "title" to state.title,
                        "content" to state.content,
                        "isPhotoAttached" to (state.photoUri != null),
                        "isAudioAttached" to (state.audioUri != null),
                    )

                    noteRepository.updateNote(state.noteId, note).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cachedPhotoUri?.let {
                                noteRepository.uploadPhoto(it, state.noteId)
                            }
                            cachedAudioUri?.let {
                                noteRepository.uploadAudio(it, state.noteId)
                            }
                            state = state.copy(success = true)
                        } else {
                            Log.e("EditNoteViewModel", "Failed to update note.", task.exception)
                        }
                    }
                }
            }

            is EditNoteFormEvent.Cancel -> {
                state = EditNoteState()
            }
        }
    }
}

sealed class EditNoteFormEvent {
    data class TitleChanged(val title: String) : EditNoteFormEvent()
    data class ContentChanged(val content: String) : EditNoteFormEvent()
    data class PhotoAttached(val uri: Uri?) : EditNoteFormEvent()
    data class AudioAttached(val uri: Uri?) : EditNoteFormEvent()
    object Submit : EditNoteFormEvent()
    object Cancel : EditNoteFormEvent()
}

data class EditNoteState(
    val noteId: String = "",
    val title: String = "",
    val content: String = "",
    val photoUri: Uri? = null,
    val audioUri: Uri? = null,
    val titleError: UiText? = null,
    val success: Boolean = false
)