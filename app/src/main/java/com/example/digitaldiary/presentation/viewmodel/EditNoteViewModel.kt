package com.example.digitaldiary.presentation.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.R
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.domain.ValidateTitleUseCase
import com.example.digitaldiary.presentation.event.EditNoteFormEvent
import com.example.digitaldiary.presentation.state.EditNoteState
import com.example.digitaldiary.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val validateTitleUseCase: ValidateTitleUseCase
) : ViewModel() {

    var state by mutableStateOf(EditNoteState())

    fun loadNoteDetails(noteId: String) {
        viewModelScope.launch {
            try {

                val noteDeferred = async { noteRepository.getNoteById(noteId).await() }
                val photoDeferred =
                    async { runCatching { noteRepository.getPhotoUrl(noteId).await() }.getOrNull() }
                val audioDeferred =
                    async { runCatching { noteRepository.getAudioUrl(noteId).await() }.getOrNull() }

                val note = noteDeferred.await()
                val photoUrl = photoDeferred.await()
                val audioUrl = audioDeferred.await()

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
                if (event.uri == null) {
                    deletePhotoFromServer(state.noteId)
                }
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
                            if (cachedPhotoUri == null) {
                                deletePhotoFromServer(state.noteId)
                            }

                            if (cachedAudioUri == null) {
                                deleteAudioFromServer(state.noteId)
                            }

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

    private fun deletePhotoFromServer(noteId: String) {
        viewModelScope.launch {
            try {
                noteRepository.deletePhoto(noteId).await()
            } catch (e: Exception) {
                Log.e("EditNoteViewModel", "Failed to delete photo", e)
            }
        }
    }

    private fun deleteAudioFromServer(noteId: String) {
        viewModelScope.launch {
            try {
                noteRepository.deleteAudio(noteId).await()
            } catch (e: Exception) {
                Log.e("EditNoteViewModel", "Failed to delete photo", e)
            }
        }
    }
}

