package com.example.digitaldiary.data;

import android.net.Uri
import android.util.Log
import com.example.digitaldiary.domain.NoteRepository
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage


class NoteRepositoryImpl : NoteRepository {
    private val database = Firebase.database
    private val notesRef = database.getReference("notes")
    val storageRef = Firebase.storage.reference

    override fun addNote(note: Map<String, Any>): Task<String> {
        val noteId = notesRef.push().key
            ?: return Tasks.forException(IllegalStateException("Couldn't generate note ID"))
        val noteWithId = note.toMutableMap()
        noteWithId["id"] = noteId
        val taskCompletionSource = TaskCompletionSource<String>()

        notesRef.child(noteId).setValue(noteWithId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                taskCompletionSource.setResult(noteId)
            } else {
                task.exception?.let {
                    taskCompletionSource.setException(it)
                } ?: taskCompletionSource.setException(IllegalStateException("Unknown error occurred"))
            }
        }

        return taskCompletionSource.task
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

    override fun uploadPhoto(uri: Uri, noteId: String): Task<Void> {
        val taskCompletionSource = TaskCompletionSource<Void>()
        val uploadTask = storageRef.child("note_photos/$noteId").putFile(uri)

        uploadTask.addOnSuccessListener {
            taskCompletionSource.setResult(null)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Image Upload failed", exception)
            taskCompletionSource.setException(exception)
        }

        return taskCompletionSource.task
    }

    override fun uploadAudio(uri: Uri, noteId: String): Task<Void> {
        val taskCompletionSource = TaskCompletionSource<Void>()
        val uploadTask = storageRef.child("note_audios/$noteId").putFile(uri)

        uploadTask.addOnSuccessListener {
            taskCompletionSource.setResult(null)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Audio Upload failed", exception)
            taskCompletionSource.setException(exception)
        }

        return taskCompletionSource.task
    }

    override fun getNoteById(noteId: String): Task<NotePreview?> {
        val taskCompletionSource = TaskCompletionSource<NotePreview?>()

        notesRef.child(noteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val note = snapshot.getValue(NotePreview::class.java)
                    taskCompletionSource.setResult(note)
                } else {
                    taskCompletionSource.setResult(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                taskCompletionSource.setException(error.toException())
            }
        })

        return taskCompletionSource.task
    }

    override fun getPhotoUrl(noteId: String): Task<Uri> {
        val photoRef = storageRef.child("note_photos/$noteId")
        return photoRef.downloadUrl
    }

    override fun getAudioUrl(noteId: String): Task<Uri> {
        val audioRef = storageRef.child("note_audios/$noteId")
        return audioRef.downloadUrl
    }

    override fun updateNote(noteId: String, note: Map<String, Any>): Task<Void> {
        val taskCompletionSource = TaskCompletionSource<Void>()

        notesRef.child(noteId).updateChildren(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                taskCompletionSource.setResult(null)
            } else {
                task.exception?.let {
                    taskCompletionSource.setException(it)
                } ?: taskCompletionSource.setException(IllegalStateException("Unknown error occurred"))
            }
        }

        return taskCompletionSource.task
    }

    override fun deletePhoto(noteId: String): Task<Void> {
        val taskCompletionSource = TaskCompletionSource<Void>()
        val photoRef = storageRef.child("note_photos/$noteId")

        photoRef.delete().addOnSuccessListener {
            taskCompletionSource.setResult(null)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Image Deletion failed", exception)
            taskCompletionSource.setException(exception)
        }

        return taskCompletionSource.task
    }


}