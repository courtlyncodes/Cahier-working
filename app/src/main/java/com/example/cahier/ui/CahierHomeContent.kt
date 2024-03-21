package com.example.cahier.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cahier.R
import com.example.cahier.data.LocalNotesDataProvider
import com.example.cahier.data.Note
import com.example.cahier.ui.theme.CahierTheme
import java.time.format.DateTimeFormatter


@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    val date = note.lastModified
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_pattern))
    val formattedDate = date.format(formatter)
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(6.dp)
    ) {
        Image(
            painterResource(R.drawable.media),
            contentDescription = null,
            modifier = Modifier.heightIn(115.dp)
        )
        Column(modifier.padding(16.dp)) {
            Text(note.title)
            Text(formattedDate.toString())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CahierTopAppBar() {
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
@Preview(showBackground = true)
@Composable
fun CahierHomeContentPreview() {
    CahierTheme {
        NoteItem(note = LocalNotesDataProvider.allNotes.first())
    }
}