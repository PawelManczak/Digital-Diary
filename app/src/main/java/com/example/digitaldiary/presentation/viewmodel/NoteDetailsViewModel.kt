package com.example.digitaldiary.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.presentation.state.NoteDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val noteId: String = savedStateHandle["id"] ?: throw IllegalArgumentException("Missing note ID")

    private val _state = mutableStateOf(NoteDetailsState())
    val state = _state

    init {
        fetchNoteDetails(noteId)
    }

    fun fetchNoteDetails(noteId: String) {
        viewModelScope.launch {
            try {
                val noteDeferred = async { noteRepository.getNoteById(noteId).await() }
                val photoDeferred = async { runCatching { noteRepository.getPhotoUrl(noteId).await() }.getOrNull() }
                val audioDeferred = async { runCatching { noteRepository.getAudioUrl(noteId).await() }.getOrNull() }

                val note = noteDeferred.await()
                val photoUrl = photoDeferred.await()
                val audioUrl = audioDeferred.await()

                _state.value = NoteDetailsState(
                    isLoading = false,
                    note = note,
                    photoUrl = photoUrl,
                    audioUrl = audioUrl
                )
            } catch (e: Exception) {
                Log.e("NoteDetailsViewModel", "Failed to fetch note details", e)
                _state.value = NoteDetailsState(isLoading = false)
            }
        }
    }


}


