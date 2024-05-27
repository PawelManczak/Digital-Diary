package com.example.digitaldiary.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.digitaldiary.presentation.components.FullScreenLoadingIndicator
import com.example.digitaldiary.presentation.viewmodel.NoteDetailsState
import com.example.digitaldiary.presentation.viewmodel.NoteDetailsViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun NoteDetailsScreen(id: String) {

    val vm = hiltViewModel<NoteDetailsViewModel>()

    if (vm.state.value.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        NoteDetailsContent(vm.state.value)


    }


}

@Composable
fun NoteDetailsContent(state: NoteDetailsState) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Note Details", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            Text(text = state.note!!.title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
            Text(text = state.note.city, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(state.note!!.content, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        state.photoUrl?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Note Photo",
                modifier = Modifier
                    .height(200.dp)
                    .shadow(15.dp)
            )
        }
    }
}