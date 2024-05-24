package com.example.digitaldiary.domain

import com.example.digitaldiary.data.NotePreview
import com.google.android.gms.tasks.Task


interface NoteRepository {
    fun addNote(note: Map<String, Any>): Task<Void>
    fun getAllNotes(): Task<List<NotePreview>>
}