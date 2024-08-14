package com.example.cahier.ui.viewmodels


import android.util.Log
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import com.example.cahier.ui.NoteCanvasDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CanvasScreenViewModel(
    // Dependency injection for the NotesRepository
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NotesRepository
) : ViewModel() {

    private val _note = MutableStateFlow(CahierUiState())
    val note: StateFlow<CahierUiState> = _note.asStateFlow()

    // Get the note ID from the saved state handle
    // NoteId is from the navigation graph
    private val noteId: Long? = savedStateHandle[NoteCanvasDestination.NOTE_ID_ARG]


    private val _existingPaths = MutableStateFlow<List<Path>>(emptyList())
    val existingPaths: StateFlow<List<Path>> = _existingPaths.asStateFlow()

    // Initialize the ViewModel to get a note by its ID
    init {
        viewModelScope.launch {
            noteRepository.getNoteStream(noteId!!)
                .filterNotNull()
                .collect { note ->
                    _note.value = CahierUiState(note)
                    loadExistingPaths(note.id)
                }
        }
    }

    fun updateNoteTitle(title: String) {
        try {
            updateUiState(note.value.note.copy(title = title))
            viewModelScope.launch {
                noteRepository.updateNote(note.value.note.copy(title = title))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating note: ${e.message}")
        }
    }

    fun updateNoteText(text: String) {
        try {
            updateUiState(note.value.note.copy(text = text))
            viewModelScope.launch {
                noteRepository.updateNote(note.value.note.copy(text = text))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating note: ${e.message}")
        }
    }

    private fun updateUiState(note: Note) {
        try {
            _note.update { it.copy(note = note) }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating note: ${e.message}")
        }
    }

    // Function to load existing paths for a note from the saved stylus drawings
    private fun loadExistingPaths(noteId: Long) {
        try {
            viewModelScope.launch {
                noteRepository.getDrawingsForNote(noteId).collect { drawings ->
                    _existingPaths.update {
                        return@update  drawings
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading existing paths: ${e.message}")
        }
    }


    companion object {
        private const val TAG = "CanvasScreenViewModel"
    }

    private fun loadExistingPaths(noteId: Long) {
        viewModelScope.launch {
            noteRepository.getDrawingsForNote(noteId).collect { drawings ->
                _existingPaths.update {
                    return@update  drawings
                }
            }
        }
    }
}