package com.example.digitaldiary.presentation.state

import com.example.digitaldiary.data.NotePreview

data class NotesListState (
    val isLoading: Boolean = false,
    val notesList: List<NotePreview> = emptyList()
)