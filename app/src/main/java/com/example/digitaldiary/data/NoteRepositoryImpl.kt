package com.example.digitaldiary.data;

import com.example.digitaldiary.domain.NoteRepository
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class NoteRepositoryImpl : NoteRepository {
    private val database = Firebase.database
    private val notesRef = database.getReference("notes")

    override fun addNote(note: Map<String, Any>): Task<Void> {
        val noteId = notesRef.push().key
            ?: return Tasks.forException(IllegalStateException("Couldn't generate note ID"))
        val noteWithId = note.toMutableMap()
        noteWithId["id"] = noteId
        return notesRef.child(noteId).setValue(noteWithId)
    }

    override fun getAllNotes(): Task<List<NotePreview>> {
        val taskCompletionSource = TaskCompletionSource<List<NotePreview>>()

        notesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList = mutableListOf<NotePreview>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(NotePreview::class.java)
                    if (note != null) {
                        notesList.add(note)
                    }
                }
                taskCompletionSource.setResult(notesList)
            }

            override fun onCancelled(error: DatabaseError) {
                taskCompletionSource.setException(error.toException())
            }
        })

        return taskCompletionSource.task
    }
}