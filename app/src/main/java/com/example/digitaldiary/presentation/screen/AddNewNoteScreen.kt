package com.example.digitaldiary.presentation.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.presentation.event.AddNewNoteFormEvent
import com.example.digitaldiary.presentation.viewmodel.AddNewNoteViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun AddNewNoteScreen() {

    val vm = hiltViewModel<AddNewNoteViewModel>()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add new note", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Title")
        TextField(modifier = Modifier.fillMaxWidth(),
            value = vm.state.title,
            onValueChange = { vm.onEvent(AddNewNoteFormEvent.TitleChanged(it)) })
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Content")
        TextField(modifier = Modifier.fillMaxWidth(),
            value = vm.state.content,
            onValueChange = { vm.onEvent(AddNewNoteFormEvent.ContentChanged(it)) })

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { vm.onEvent(AddNewNoteFormEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
        }
    }

}