package com.example.cahier.data

import androidx.compose.ui.graphics.painter.Painter
import java.util.Date

data class Note (
    val id: Int?,
    val name: String?,
    val description: String?,
    val date: Date?,
    val image: Painter? /*(painter? int?)*/
)