package com.example.digitaldiary.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.R
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.presentation.components.FullScreenLoadingIndicator
import com.example.digitaldiary.presentation.listitem.NotesItem
import com.example.digitaldiary.presentation.screen.destinations.AddNewNoteScreenDestination
import com.example.digitaldiary.presentation.screen.destinations.MapScreenDestination
import com.example.digitaldiary.presentation.screen.destinations.NoteDetailsScreenDestination
import com.example.digitaldiary.presentation.viewmodel.MainScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination(start = true)
fun MainScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNotes()
    }

    if (state.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        MainScreenContent(state.notesList, onNoteClick = { noteId ->
            navigator.navigate(NoteDetailsScreenDestination(noteId))
        }, onAddNewClick = {
            navigator.navigate(AddNewNoteScreenDestination)
        }, onMapScreenClick = {
            navigator.navigate(MapScreenDestination)
        })
    }
}

@Composable
fun MainScreenContent(
    notesList: List<NotePreview>,
    onNoteClick: (String) -> Unit,
    onAddNewClick: () -> Unit,
    onMapScreenClick: () -> Unit
) {
    Box {
        LazyColumn {
            items(notesList) { note ->
                NotesItem(note = note, onNoteClick = onNoteClick)
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onAddNewClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Default.Add, stringResource(R.string.add_note))
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            onClick = onMapScreenClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Default.Place, "map")
        }
    }
}
