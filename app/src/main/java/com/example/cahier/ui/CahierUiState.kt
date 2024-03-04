package com.example.cahier.ui

import android.provider.ContactsContract.CommonDataKinds.Note

class CahierUiState (
    val note: Note,
    val notes: List<Note> = emptyList()
)