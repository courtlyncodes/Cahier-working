package com.example.cahier.ui.viewmodels

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
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NotesRepository
) : ViewModel() {

    private val _note = MutableStateFlow(CahierUiState())
    val note: StateFlow<CahierUiState> = _note.asStateFlow()

    private val noteId: Long? = savedStateHandle[NoteCanvasDestination.NOTE_ID_ARG]


    private val _existingPaths = MutableStateFlow<List<Path>>(emptyList())
    val existingPaths: StateFlow<List<Path>> = _existingPaths.asStateFlow()

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
            viewModelScope.launch {
                noteRepository.updateNote(note.value.note.copy(title = title))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateNoteText(text: String) {
        try {
            viewModelScope.launch {
                noteRepository.updateNote(note.value.note.copy(text = text))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateUiState(note: Note) {
        _note.update { it.copy(note = note) }
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