package com.example.digitaldiary.presentation.state

import android.net.Uri
import com.example.digitaldiary.presentation.util.UiText

data class EditNoteState(
    val noteId: String = "",
    val title: String = "",
    val content: String = "",
    val photoUri: Uri? = null,
    val audioUri: Uri? = null,
    val titleError: UiText? = null,
    val success: Boolean = false
)