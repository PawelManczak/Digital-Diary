package com.example.digitaldiary.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.icon.Camera
import com.example.digitaldiary.presentation.screencontent.CameraContent
import com.example.digitaldiary.presentation.viewmodel.AddNewNoteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
fun AddNewNoteScreen(navigator: DestinationsNavigator) {

    val vm = hiltViewModel<AddNewNoteViewModel>()

    var showCamera by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val audioPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val locationPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)





    if (vm.state.success) {
        LaunchedEffect(true) {
            navigator.popBackStack()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_new_note),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.title))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.state.title,
            onValueChange = { vm.onEvent(AddNewNoteFormEvent.TitleChanged(it)) },
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
            onValueChange = { vm.onEvent(AddNewNoteFormEvent.ContentChanged(it)) })

        Spacer(modifier = Modifier.weight(1f))

        if(vm.state.photoUri != null) {
            AsyncImage(
                model = vm.state.photoUri,
                contentDescription = null,
                modifier = Modifier.height(300.dp).shadow(15.dp),
                alignment = Alignment.TopCenter
            )

            Spacer(Modifier.height(8.dp))
        }

        Row {
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "attach photo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { showCamera = true }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(id = R.drawable.sound),
                contentDescription = "attach audio",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!locationPermissionState.status.isGranted) {
                    locationPermissionState.launchPermissionRequest()
                }

                if (locationPermissionState.status.isGranted) {
                    vm.onEvent(AddNewNoteFormEvent.Submit)
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
                vm.onEvent(AddNewNoteFormEvent.PhotoAttached(it))
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

