package com.example.digitaldiary.presentation.state

import android.net.Uri
import com.example.digitaldiary.data.NotePreview

data class NoteDetailsState(
    val isLoading: Boolean = true,
    val note: NotePreview? = null,
    val photoUrl: Uri? = null,
    val audioUrl: Uri? = null
)