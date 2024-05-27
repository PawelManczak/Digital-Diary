package com.example.digitaldiary.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.digitaldiary.R
import com.example.digitaldiary.presentation.icon.Camera
import com.example.digitaldiary.presentation.screen.destinations.MainScreenDestination
import com.example.digitaldiary.presentation.screencontent.CameraContent
import com.example.digitaldiary.presentation.util.playback.AndroidAudioPlayer
import com.example.digitaldiary.presentation.util.record.AndroidAudioRecorder
import com.example.digitaldiary.presentation.event.EditNoteFormEvent
import com.example.digitaldiary.presentation.viewmodel.EditNoteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.File

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
fun EditNoteScreen(navigator: DestinationsNavigator, noteId: String) {

    val vm = hiltViewModel<EditNoteViewModel>()

    LaunchedEffect(noteId) {
        vm.loadNoteDetails(noteId)
    }

    var showCamera by remember { mutableStateOf(false) }
    var showAudioRecorder by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val audioPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val locationPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val recorder = remember { AndroidAudioRecorder(applicationContext) }
    val player = remember { AndroidAudioPlayer(applicationContext) }

    var audioFile: File? by remember { mutableStateOf(null) }

    if (vm.state.success) {
        LaunchedEffect(true) {
            navigator.popBackStack(
                MainScreenDestination.route, inclusive = false
            )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.edit_note),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.title))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.state.title,
            onValueChange = { vm.onEvent(EditNoteFormEvent.TitleChanged(it)) },
            isError = vm.state.titleError != null,
            supportingText = {
                if (vm.state.titleError != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = vm.state.titleError!!.asString(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = stringResource(R.string.content))

        TextField(modifier = Modifier.fillMaxWidth(),
            value = vm.state.content,
            onValueChange = { vm.onEvent(EditNoteFormEvent.ContentChanged(it)) })

        Spacer(modifier = Modifier.weight(1f))

        if (vm.state.photoUri != null) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .shadow(15.dp)
            ) {
                AsyncImage(
                    model = vm.state.photoUri,
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .shadow(15.dp),
                    alignment = Alignment.TopCenter
                )
                IconButton(onClick = { vm.onEvent(EditNoteFormEvent.PhotoAttached(null)) }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.remove_photo),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Icon(imageVector = Icons.Default.Camera,
                contentDescription = stringResource(R.string.attach_photo),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { showCamera = true })

            Spacer(modifier = Modifier.width(8.dp))

            Icon(painter = painterResource(id = R.drawable.sound),
                contentDescription = stringResource(R.string.attach_audio),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        if (!audioPermissionState.status.isGranted) {
                            audioPermissionState.launchPermissionRequest()
                        }

                        if (audioPermissionState.status.isGranted) {
                            showAudioRecorder = true
                        }
                    })

            if (vm.state.audioUri != null) {
                Button(onClick = { vm.onEvent(EditNoteFormEvent.AudioAttached(null)) }) {
                    Text(text = stringResource(R.string.remove_attached_audio))
                }
            }
            Spacer(Modifier.weight(1f))

        }

        if (showAudioRecorder) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    val cacheDir = applicationContext.cacheDir
                    File(cacheDir, "audio.mp3").also {
                        recorder.start(it)
                        audioFile = it
                    }
                }) {
                    Text(text = stringResource(R.string.start_recording))
                }
                Button(onClick = {
                    recorder.stop()
                    audioFile?.let {
                        vm.onEvent(EditNoteFormEvent.AudioAttached(Uri.fromFile(it)))
                    }
                    showAudioRecorder = false
                }) {
                    Text(text = stringResource(R.string.stop_recording))
                }
                Button(onClick = {
                    player.playFile(audioFile ?: return@Button)
                }) {
                    Text(text = stringResource(R.string.play))
                }
                Button(onClick = {
                    player.stop()
                }) {
                    Text(text = stringResource(R.string.stop_playing))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!locationPermissionState.status.isGranted) {
                    locationPermissionState.launchPermissionRequest()
                }

                if (locationPermissionState.status.isGranted) {
                    vm.onEvent(EditNoteFormEvent.Submit)
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.location_permission_is_required_to_add_a_note),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(
                text = stringResource(R.string.submit), style = MaterialTheme.typography.titleMedium
            )
        }
    }

    if (showCamera) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }

        if (cameraPermissionState.status.isGranted) {
            CameraContent(onPhotoCaptured = {
                vm.onEvent(EditNoteFormEvent.PhotoAttached(it))
                showCamera = false
            })
        } else {
            showCamera = false
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_is_required_to_add_a_photo),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
