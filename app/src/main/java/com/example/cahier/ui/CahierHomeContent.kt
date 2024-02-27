package com.example.cahier.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cahier.data.Note
import com.example.cahier.ui.theme.CahierTheme

@Composable
fun CahierNoteListItem(
//    note: Note,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxSize()
//        can i make the size of these cards respond to the screen size
        //Lazylist
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun CahierHomeContentPreview() {
    CahierTheme {
        CahierNoteListItem()
    }
}