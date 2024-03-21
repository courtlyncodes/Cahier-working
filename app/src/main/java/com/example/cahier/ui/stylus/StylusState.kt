package com.example.cahier.ui.stylus

import android.graphics.Path

data class StylusState (
    val pressure: Float = 0F,
    val orientation: Float = 0F,
    val tilt: Float = 0F,
    val path: Path = Path()
)