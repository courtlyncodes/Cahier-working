package com.example.cahier.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

data class SavedPath(
    val path: Path,
    val color: Color,
    val style: Stroke
)