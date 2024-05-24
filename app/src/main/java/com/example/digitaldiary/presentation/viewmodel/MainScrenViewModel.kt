package com.example.digitaldiary.presentation.viewmodel

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.presentation.state.NotesListState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.accompanist.permissions.rememberPermissionState

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(NotesListState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        getNotes()
    }

    fun getNotes() {
        _state.update { it.copy(isLoading = true) }
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
