package com.example.digitaldiary.data

data class NotePreview(
    val id: Int,
    val title: String,
    val content: String,
    val isPhotoAttached: Boolean,
    val isAudioAttached: Boolean,
)