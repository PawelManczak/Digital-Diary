package com.example.digitaldiary.presentation.event

import android.net.Uri

sealed class EditNoteFormEvent {
    data class TitleChanged(val title: String) : EditNoteFormEvent()
    data class ContentChanged(val content: String) : EditNoteFormEvent()
    data class PhotoAttached(val uri: Uri?) : EditNoteFormEvent()
    data class AudioAttached(val uri: Uri?) : EditNoteFormEvent()
    object Submit : EditNoteFormEvent()
    object Cancel : EditNoteFormEvent()
}