package com.example.cahier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteDetailViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CahierUiState())
    val uiState: StateFlow<CahierUiState> = _uiState.asStateFlow()

    fun updateUiState(note: Note) {
        _uiState.update { it.copy(note = note) }
    }

    suspend fun updateNote(note: Note) {
        noteRepository.updateNote(note)
    }

    suspend fun addNote(): Long {
        return noteRepository.addNote(_uiState.value.note)
    }

    suspend fun deleteNote() {
        noteRepository.deleteNote(_uiState.value.note)
    }

    fun resetUiState() {
        val newNote = Note(id = 0, title = "", text = "", image = null)
        _uiState.value = CahierUiState(newNote)
    }
}