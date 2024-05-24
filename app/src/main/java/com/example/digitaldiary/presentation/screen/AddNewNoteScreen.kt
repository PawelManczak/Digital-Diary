package com.example.digitaldiary.presentation.screen

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.R
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.viewmodel.AddNewNoteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
fun AddNewNoteScreen(navigator: DestinationsNavigator) {

    val vm = hiltViewModel<AddNewNoteViewModel>()

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

}