package com.example.cahier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cahier.R
import com.example.cahier.data.LocalNotesDataProvider
import com.example.cahier.data.Note
import com.example.cahier.ui.theme.CahierTheme

@Composable
fun CahierList(
    notes: List<Note>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Create, "large floating action button.")
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(184.dp),
            modifier.padding(innerPadding)
        )
        {
            items(notes.size) { note ->
                CahierNoteListItem(note = notes[note])
            }
        }
    }
}

@Composable
fun CahierNoteListItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(6.dp)
    ) {
        Image(
            painterResource(R.drawable.media),
            contentDescription = "Card media",
            modifier = Modifier
                .heightIn(115.dp)
                .background(Color(0xFFDADCE0))
        )
        Column(modifier.padding(16.dp)) {
            Text(note.title)
            Text(note.date)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CahierHomeContentPreview() {
    CahierTheme {
        CahierList(notes = LocalNotesDataProvider.allNotes)
    }
}