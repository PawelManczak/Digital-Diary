package com.example.digitaldiary.domain

import com.google.android.gms.tasks.Task


interface NoteRepository {
    fun addNote(note: Map<String, String>): Task<Void>
}