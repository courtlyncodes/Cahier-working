package com.example.cahier.ui

import androidx.lifecycle.ViewModel
import com.example.cahier.data.CanvasUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CanvasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CanvasUiState())
    val uiState: StateFlow<CanvasUiState> = _uiState.asStateFlow()

    fun updateText(text: String) {
        _uiState.value = _uiState.value.copy(text = text)
    }
}
