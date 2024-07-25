package com.example.cahier.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteListViewModel(private val noteRepository: NotesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<Note?>(null)
    val uiState: StateFlow<Note?> = _uiState

    private var newlyAddedId = 0L

    /**
     * Holds ui state for the list of notes on the home pane. The list of items are retrieved from [NoteRepository] and mapped to
     * [NoteListUiState]
     */
    val noteList: StateFlow<NoteListUiState> =
        noteRepository.getAllNotesStream().map { NoteListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteListUiState()
            )

    fun selectNote(noteId: Long) {
        try {
            viewModelScope.launch {
                noteRepository.getNoteStream(noteId).collect {
                    _uiState.value = it
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun addNote(callback: (id: Long) -> Unit): Long {
        try {
            viewModelScope.launch {
                resetUiState()
                newlyAddedId = noteRepository.addNote(_uiState.value!!)
                _uiState.value =
                    _uiState.value!!.copy(id = newlyAddedId)
                callback(newlyAddedId)
            }
            return newlyAddedId
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteNote() {
        try {
            viewModelScope.launch {
                noteRepository.deleteNote(_uiState.value!!)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun resetUiState() {
        val newNote = Note(id = 0, title = "", text = "", image = null)
        _uiState.value = newNote
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class NoteListUiState(val noteList: List<Note> = listOf())