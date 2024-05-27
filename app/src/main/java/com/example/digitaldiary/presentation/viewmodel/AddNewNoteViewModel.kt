package com.example.digitaldiary.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaldiary.R
import com.example.digitaldiary.domain.NoteRepository
import com.example.digitaldiary.domain.ValidateTitleUseCase
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.state.AddNewNoteState
import com.example.digitaldiary.presentation.util.UiText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteRepository,
    private val validateTitleUseCase: ValidateTitleUseCase
) : ViewModel() {


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    var state by mutableStateOf(AddNewNoteState())

    fun onEvent(event: AddNewNoteFormEvent) {
        when (event) {
            is AddNewNoteFormEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }

            is AddNewNoteFormEvent.ContentChanged -> {
                state = state.copy(content = event.content)
            }

            is AddNewNoteFormEvent.PhotoAttached -> {
                state = state.copy(photoUri = event.uri)
            }

            is AddNewNoteFormEvent.AudioAttached -> {
                state = state.copy(audioUri = event.uri)
            }

            is AddNewNoteFormEvent.Submit -> {

                if (!validateTitleUseCase(state.title)) {
                    state =
                        state.copy(titleError = UiText.StringResource(R.string.title_cannot_be_empty))
                    return
                }

                val cachedPhotoUri = state.photoUri
                val cachedAudioUri = state.audioUri

                viewModelScope.launch {

                    val note = mapOf(
                        "title" to state.title,
                        "content" to state.content,
                        "isPhotoAttached" to (state.photoUri != null),
                        "isAudioAttached" to (state.audioUri != null),
                        "city" to getCurrentCityName(),
                    )


                    noteRepository.addNote(note).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MainActivity", task.result.toString())
                            val noteId = task.result.toString()
                            cachedPhotoUri?.let {
                                noteRepository.uploadPhoto(it, noteId)
                            }
                            cachedAudioUri?.let {
                                noteRepository.uploadAudio(it, noteId)
                            }
                            state = state.copy(success = true)
                        } else {
                            Log.e("MainActivity", "Failed to add note.", task.exception)
                        }
                    }

                    state = AddNewNoteState()
                }


            }

            is AddNewNoteFormEvent.Cancel -> {
                state = AddNewNoteState()
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentCityName(): String = withContext(Dispatchers.IO) {
        var cityName = "Unknown Location"
        val location: Location? = fusedLocationClient.lastLocation.await()
        location?.let {
            val geocoder = Geocoder(application.applicationContext, Locale.getDefault())
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            cityName = addresses?.get(0)?.locality ?: "Unknown Location"
        }
        return@withContext cityName
    }
}

