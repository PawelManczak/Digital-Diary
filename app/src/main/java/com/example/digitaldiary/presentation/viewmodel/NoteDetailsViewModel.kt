package com.example.digitaldiary.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private fun fetchNoteDetails(noteId: String) {
        viewModelScope.launch {
            try {
                val note = noteRepository.getNoteById(noteId).await()
                val photoUrl = noteRepository.getPhotoUrl(noteId).await()
                _state.value = NoteDetailsState(isLoading = false, note = note, photoUrl = photoUrl)
            } catch (e: Exception) {
                Log.e("NoteDetailsViewModel", "Failed to fetch note details", e)
            }
        }
    }


}

data class NoteDetailsState(
    val isLoading: Boolean = true,
    val note: NotePreview? = null,
    val photoUrl: Uri? = null
)


