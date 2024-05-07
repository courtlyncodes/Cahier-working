package com.example.cahier.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
//    private val _uiState = MutableStateFlow(CahierUiState())
//    var uiState: StateFlow<CahierUiState> = _uiState.asStateFlow()
    var uiState by mutableStateOf(CahierUiState())
        private set

    /**
     * Updates the [uiState] with the note values provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(note: Note) {
        uiState = CahierUiState(note)
    }

    /**
     * Inserts an [Note] in the Room database
     */
    suspend fun addNote() {
        notesRepository.addNote(uiState.note)
    }


    suspend fun updateNote() {
            notesRepository.updateNote(uiState.note)
        }


    /**
     * Resets the canvas text fields
     */
    fun resetUiState() {
       val newNote = Note(id = 0, title = "", text = "", image = null)
        uiState = CahierUiState(newNote)
    }
}