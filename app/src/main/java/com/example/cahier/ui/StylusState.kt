package com.example.cahier.ui

import androidx.compose.ui.graphics.Path

data class StylusState(
    val pressure: Float = 0F,
    val orientation: Float = 0F,
    val tilt: Float = 0F,
    val path: Path = Path()
)