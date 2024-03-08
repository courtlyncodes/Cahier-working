package com.example.cahier.ui

import androidx.lifecycle.ViewModel
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CahierViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CahierUiState())
    val uiState: StateFlow<CahierUiState> = _uiState.asStateFlow()

    fun updateNote(note: Note?) {
        _uiState.update { currentState ->
            currentState.copy(note = note)
        }
    }
}