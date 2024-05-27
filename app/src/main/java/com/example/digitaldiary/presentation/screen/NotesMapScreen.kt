package com.example.digitaldiary.presentation.screen


import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.presentation.viewmodel.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination

private val POLAND_CENTER_LATLNG = LatLng(52.215933, 19.134422)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
fun MapScreen() {
    val viewModel: MapViewModel = hiltViewModel()
    val state = viewModel.state.value

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(POLAND_CENTER_LATLNG, 5.5f)
    }

    if (locationPermissionState.allPermissionsGranted) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            state.notes.forEach { note ->
                Marker(
                    state = MarkerState(position = LatLng(note.latitude, note.longitude)),
                    title = note.title,
                    snippet = note.content
                )
            }
        }
    } else {
        LaunchedEffect(Unit) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }
}