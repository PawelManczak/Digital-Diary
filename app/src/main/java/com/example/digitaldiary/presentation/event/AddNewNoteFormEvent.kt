package com.example.digitaldiary.presentation.event

sealed class AddNewNoteFormEvent {

    data class TitleChanged(val title: String) : AddNewNoteFormEvent()
    data class ContentChanged(val content: String) : AddNewNoteFormEvent()
    data class PhotoAttached(val isAttached: Boolean) : AddNewNoteFormEvent()
    data class AudioAttached(val isAttached: Boolean) : AddNewNoteFormEvent()
    data object Submit : AddNewNoteFormEvent()
    data object Cancel : AddNewNoteFormEvent()
}
