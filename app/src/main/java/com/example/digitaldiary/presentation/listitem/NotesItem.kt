package com.example.digitaldiary.presentation.listitem

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.digitaldiary.R
import com.example.digitaldiary.data.NotePreview
import com.example.digitaldiary.presentation.icon.Camera

@Composable
fun NotesItem(
    note: NotePreview, onNoteClick: (Int) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onNoteClick(note.id) }
        .padding(16.dp)
        .border(
            1.dp,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            RoundedCornerShape(8.dp)
        )
    ) {
        Text(
            text = note.title, style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = note.content, style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row{
            if(note.isPhotoAttached) {
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Photo attached",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            if(note.isAudioAttached) {
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.sound),
                    contentDescription = "Audio attached",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

    }
}

@Composable
@Preview
fun NotesItemPreview() {
    NotesItem(
        note = NotePreview(
            id = 1,
            title = "Title",
            content = "Content",
            isPhotoAttached = true,
            isAudioAttached = true
        )
    ) {}
}