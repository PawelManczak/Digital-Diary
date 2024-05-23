package com.example.digitaldiary.presentation.state

import android.net.Uri
import com.example.digitaldiary.presentation.util.UiText

data class AddNewNoteState(
    val title: String = "",
    val titleError: UiText? = null,
    val content: String = "",
    val contentError: UiText? = null,
    val photoUri: Uri? = null,
    val audioUri: Uri? = null,
)