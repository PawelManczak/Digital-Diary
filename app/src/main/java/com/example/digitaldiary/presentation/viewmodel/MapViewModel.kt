package com.example.digitaldiary.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    var state = mutableStateOf(MapState())

    fun loadNotes() {
        viewModelScope.launch {
            try {
                val notes = noteRepository.getAllNotes().await()
                state.value = state.value.copy(notes = notes)
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error loading notes", e)
            }
        }
    }
}

data class MapState(
    val notes: List<NotePreview> = emptyList()
)