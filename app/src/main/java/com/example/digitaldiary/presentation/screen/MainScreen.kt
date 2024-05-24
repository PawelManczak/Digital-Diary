package com.example.digitaldiary.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.presentation.components.FullScreenLoadingIndicator
import com.example.digitaldiary.presentation.listitem.NotesItem
import com.example.digitaldiary.presentation.screen.destinations.AddNewNoteScreenDestination
import com.example.digitaldiary.presentation.viewmodel.MainScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination(start = true)
fun MainScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        MainScreenContent(state.notesList, onNoteClick = { noteId ->

        }, onAddNewClick = {
            navigator.navigate(AddNewNoteScreenDestination)
        })
    }
}

@Composable
fun MainScreenContent(
    notesList: List<NotePreview>, onNoteClick: (String) -> Unit, onAddNewClick: () -> Unit
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
            Icon(Icons.Default.Add, "add note")
        }
    }
}
