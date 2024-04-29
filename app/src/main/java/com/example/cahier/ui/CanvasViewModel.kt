package com.example.cahier.ui

import androidx.lifecycle.ViewModel
import com.example.cahier.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CanvasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Note(id = 0, title = "", text = "", image = 0))
    val uiState: StateFlow<Note> = _uiState.asStateFlow()

    fun updateTitle(text: String) {
        _uiState.value = _uiState.value.copy(title = text)
    }
    fun updateDescription(text: String) {
        _uiState.value = _uiState.value.copy(text = text)
    }
}
