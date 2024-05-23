package com.example.digitaldiary.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.digitaldiary.presentation.viewmodel.AddNewNoteViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun AddNewNoteScreen() {

    val vm = hiltViewModel<AddNewNoteViewModel>()

}