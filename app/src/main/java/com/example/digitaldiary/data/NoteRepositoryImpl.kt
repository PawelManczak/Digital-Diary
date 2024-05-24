package com.example.digitaldiary.data;

import com.example.digitaldiary.domain.NoteRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.database.database


class NoteRepositoryImpl : NoteRepository {
    private val database = Firebase.database
    private val notesRef = database.getReference("notes")

    override fun addNote(note: Map<String, String>): Task<Void> {
        return notesRef.push().setValue(note)
    }
}