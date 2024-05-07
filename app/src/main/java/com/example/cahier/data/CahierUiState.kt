package com.example.cahier.data

import kotlinx.coroutines.flow.StateFlow

data class CahierUiState(
    val note: Note =
        Note(
            id = 0,
            title = "",
            image = null,
            text = ""
        )
)