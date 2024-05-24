package com.example.digitaldiary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.presentation.state.NotesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(NotesListState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes().addOnCompleteListener {
                if (it.isSuccessful) {
                    val notes = it.result ?: emptyList()
                    _state.update { currentState ->
                        currentState.copy(notesList = notes, isLoading = false)
                    }
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
