package com.example.cahier.data

data class CahierUiState (
    val note: Note? = null,
    val notes: List<Note> = LocalNotesDataProvider.allNotes,
    val notesCount: Int = LocalNotesDataProvider.allNotes.size
)