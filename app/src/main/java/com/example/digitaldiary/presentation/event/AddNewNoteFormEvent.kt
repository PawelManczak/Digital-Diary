package com.example.digitaldiary.presentation.event

import android.net.Uri
import java.net.URI

sealed class AddNewNoteFormEvent {

    data class TitleChanged(val title: String) : AddNewNoteFormEvent()
    data class ContentChanged(val content: String) : AddNewNoteFormEvent()
    data class PhotoAttached(val uri: Uri) : AddNewNoteFormEvent()
    data class AudioAttached(val uri: Uri) : AddNewNoteFormEvent()
    data object Submit : AddNewNoteFormEvent()
    data object Cancel : AddNewNoteFormEvent()
}
