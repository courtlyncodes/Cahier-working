package com.example.cahier.ui

import android.provider.ContactsContract.CommonDataKinds.Note

class CahierUiState (
    val notes: List<Note> = emptyList()
)