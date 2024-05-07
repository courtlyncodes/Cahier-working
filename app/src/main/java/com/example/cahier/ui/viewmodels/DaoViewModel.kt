package com.example.cahier.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DaoViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "DaoViewModel"
    }
//    private val _uiState = MutableStateFlow(CahierUiState())
//    var uiState: StateFlow<CahierUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            notesRepository.getNoteStream().collect { note ->
                updateUiState(note)
            }
        }
    }

    /**
     * Holds current item ui state
     */

    var uiState by mutableStateOf(CahierUiState())
        private set

    /**
     * Updates the [uiState] with the note values provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(note: Note) {
        _uiState.update {
            it.copy(note = note)
        }
    }

    /**
     * Inserts an [Note] in the Room database
     */
//    suspend fun addNote() {
//        notesRepository.addNote(uiState.note)
//    }
    fun addNote() {
        viewModelScope.launch {
            notesRepository.addNote(uiState.value.note)
        }
    }


//    suspend fun updateNote() {
//            notesRepository.updateNote(uiState.note)
//        }

    fun updateNote() {
        viewModelScope.launch {
            notesRepository.updateNote(uiState.value.note)
        }
    }

    /**
     * Resets the canvas text fields
     */
//    fun resetUiState() {
//       val newNote = Note(id = 0, title = "", text = "", image = null)
//        uiState = CahierUiState(newNote)
//    }
    fun resetUiState() {
        val newNote = Note(id = 0, title = "", text = "", image = null)
        _uiState.update {
            it.copy(note = newNote)
        }
    }
}