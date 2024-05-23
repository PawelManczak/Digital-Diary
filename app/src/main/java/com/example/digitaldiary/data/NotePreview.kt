package com.example.digitaldiary.data

data class NotePreview(
    val id: Int,
    val title: String,
    val content: String,
    val isPhotoAttached: Boolean = false,
    val isAudioAttached: Boolean = false,
)