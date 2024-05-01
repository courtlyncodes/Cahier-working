package com.example.cahier.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DaoViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    companion object { private const val TAG = "DaoViewModel" }
    /**
     * Holds current item ui state
     */
    private val _uiState = MutableStateFlow(CahierUiState())
    val uiState: StateFlow<CahierUiState> = _uiState.asStateFlow()

    /**
     * Updates the [uiState] with the note values provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateNote(note: Note) {
        _uiState.update { currentState ->
            currentState.copy(note = note)
        }
    }

    /**
     * Inserts an [Note] in the Room database
     */
    suspend fun addNote() {
        Log.wtf(TAG, "addNote")
        viewModelScope.launch {
            uiState.value.note.let { notesRepository.addNote(it) }
        }
    }

    /**
     * Resets the canvas text fields
     */
    fun resetUiState() {
       val newNote = Note(id = 0, title = "", text = "", image = null)
        _uiState.update { currentState ->
            currentState.copy(note = newNote)
        }
    }
}