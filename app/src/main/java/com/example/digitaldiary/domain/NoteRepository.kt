package com.example.digitaldiary.domain

import android.net.Uri
import com.example.digitaldiary.data.NotePreview
import com.google.android.gms.tasks.Task


interface NoteRepository {
    fun addNote(note: Map<String, Any>): Task<String>
    fun getAllNotes(): Task<List<NotePreview>>

    fun uploadPhoto(uri: Uri, noteId: String): Task<Void>

    fun getNoteById(noteId: String): Task<NotePreview?>
    fun getPhotoUrl(noteId: String): Task<Uri>
    fun uploadAudio(uri: Uri, noteId: String): Task<Void>
    fun getAudioUrl(noteId: String): Task<Uri>
    fun updateNote(noteId: String, note: Map<String, Any>): Task<Void>
    fun deletePhoto(noteId: String): Task<Void>
}