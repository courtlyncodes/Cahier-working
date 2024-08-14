package com.example.cahier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cahier.R
import com.example.cahier.data.Note

@Composable
fun NoteList(
    noteList: List<Note>,
    onAddNewNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Scaffold(topBar = {
        HomeTopAppBar(onDelete = onDelete, currentScreenName = stringResource(R.string.home))
    }, floatingActionButton = {
            IconButton(onClick = onAddNewNote, colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),) {
                Icon(Icons.Filled.Add, stringResource(R.string.floating_action_button_des))
            }
    }, modifier = modifier
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(184.dp), Modifier.padding(innerPadding)
        ) {
            items(count = noteList.size, key = { it }) { note ->
                NoteItem(
                    note = noteList[note], onNoteClick = onNoteClick
                )
            }
        }
    }
}

@Composable
fun NoteDetail(
    note: Note,
    onDelete: () -> Unit,
    onClickToEdit: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        NoteTopAppBar(onDelete = onDelete, currentScreenName = note.title)
    }) { paddingValues ->
        Column(modifier = modifier
            .clickable { onClickToEdit(note) }
            .padding(paddingValues)
        ) {
            Text(note.title)
            note.text?.let { Text(it) }
            note.image?.let { painterResource(it) }?.let {
                Image(
                    painter = it, contentDescription = note.title
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    onNoteClick: (Note) -> Unit, note: Note, modifier: Modifier = Modifier
) {
    OutlinedCard(elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(6.dp)
            .clickable { onNoteClick(note) }) {
        Image(
            painterResource(R.drawable.media),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(184.dp)
        )
        Column(modifier.padding(16.dp)) {
            Text(note.title)
        }
    }
}

// Top App Bar for a note
// Displays the title of the note and a delete button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
    onDelete: () -> Unit,
    currentScreenName: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray, titleContentColor = Color.White
        ), title = {
            Row {
                Text(currentScreenName)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onDelete() },
                    content = {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.floating_action_button_des),
                        )
                    })
            }
        })
}

// Top App Bar for Home Pane
// Displays 'Home' for the title
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onDelete: () -> Unit,
    currentScreenName: String
) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.DarkGray, titleContentColor = Color.White
    ), title = {
        Row {
            Text(currentScreenName)
        }
    })
}