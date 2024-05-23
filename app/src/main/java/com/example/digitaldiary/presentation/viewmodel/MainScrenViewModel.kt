package com.example.digitaldiary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.presentation.state.NotesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel  @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(NotesListState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _state.update {
                it.copy(
                    isLoading = false,
                    notesList = listOf(
                        NotePreview(
                            id = 1,
                            title = "First note",
                            content = "This is the first note"
                        ),
                        NotePreview(
                            id = 2,
                            title = "Second note",
                            content = "This is the second note"
                        ),
                        NotePreview(
                            id = 3,
                            title = "Third note",
                            content = "This is the third note"
                        )
                    )
                )
            }
        }
    }
}
