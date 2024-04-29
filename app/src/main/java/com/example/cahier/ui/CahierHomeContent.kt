package com.example.cahier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import java.time.format.DateTimeFormatter

@Composable
fun NoteDetail(
    note: Note,
    modifier: Modifier = Modifier
) {
   // val date = note.lastModified
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_pattern))
//    val formattedDate = date.format(formatter)

    Column {
//        Text(formattedDate)
        Text(note.title)
        note.text?.let { Text(it) }
        note.image?.let { painterResource(it) }
            ?.let {
                Image(
                    painter = it,
                    contentDescription = note.title
                )
            }
    }
}

@Composable
fun NoteItem(
    onClick: (Note) -> Unit,
    note: Note,
    modifier: Modifier = Modifier
) {
//    val date = note.lastModified
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_pattern))
//    val formattedDate = date.format(formatter)

    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(6.dp)
            .clickable { onClick(note) }
    ) {
        Image(
            painterResource(R.drawable.media),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(184.dp)
        )
        Column(modifier.padding(16.dp)) {
            Text(note.title)
//            Text(formattedDate.toString())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CahierTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            titleContentColor = Color.White
        ),
        title = {
            Text(stringResource(R.string.all_notes))
        }
    )
}