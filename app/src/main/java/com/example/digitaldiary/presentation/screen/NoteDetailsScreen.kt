package com.example.digitaldiary.presentation.screen

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.digitaldiary.R
import com.example.digitaldiary.presentation.components.FullScreenLoadingIndicator
import com.example.digitaldiary.presentation.screen.destinations.EditNoteScreenDestination
import com.example.digitaldiary.presentation.viewmodel.NoteDetailsState
import com.example.digitaldiary.presentation.viewmodel.NoteDetailsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun NoteDetailsScreen(navigator: DestinationsNavigator, id: String) {

    val vm = hiltViewModel<NoteDetailsViewModel>()

    if (vm.state.value.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        NoteDetailsContent(vm.state.value) {
            navigator.navigate(EditNoteScreenDestination(id))
        }


    }


}

@Composable
fun NoteDetailsContent(state: NoteDetailsState, onEditClick: () -> Unit) {
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(stringResource(R.string.note_details), style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = state.note!!.title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = state.note.city,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(state.note!!.content, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            state.photoUrl?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = stringResource(R.string.note_photo),
                    modifier = Modifier
                        .height(200.dp)
                        .shadow(15.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.audioUrl?.let { uri ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (isPlaying) {
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                            isPlaying = false
                        } else {
                            mediaPlayer.setDataSource(uri.toString())
                            mediaPlayer.prepare()
                            mediaPlayer.start()
                            isPlaying = true
                        }
                    }) {
                        Icon(
                            painter = if (!isPlaying) painterResource(id = R.drawable.play_arrow) else painterResource(
                                id = R.drawable.stop
                            ),
                            contentDescription = if (isPlaying) stringResource(R.string.stop) else stringResource(
                                R.string.play
                            )
                        )
                    }
                    Text(
                        text = if (isPlaying) stringResource(R.string.stop_audio) else stringResource(
                            R.string.play_audio
                        )
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onEditClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Default.Edit, stringResource(R.string.edit_note))
        }
    }

}
