package com.example.cahier.data

data class CahierUiState(
    val note: Note =
        Note(
            id = 0,
            title = "",
            image = null,
            text = ""
        )
)