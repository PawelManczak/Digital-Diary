package com.example.digitaldiary.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.presentation.components.FullScreenLoadingIndicator
import com.example.digitaldiary.presentation.viewmodel.NoteDetailsViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun NoteDetailsScreen(id: String) {

    val vm = hiltViewModel<NoteDetailsViewModel>()

    if(vm.state.value.isLoading){
        FullScreenLoadingIndicator()
    }
    else{
        NoteDetailsContent(vm.state.value.note!!)
    }


}

@Composable
fun NoteDetailsContent(note: NotePreview) {
    Column(Modifier.fillMaxSize()) {
        Text("Note Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(note.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(note.content, style = MaterialTheme.typography.bodyMedium)
    }
}
