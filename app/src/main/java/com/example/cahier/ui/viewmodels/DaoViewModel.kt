package com.example.cahier.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DaoViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {
    companion object {
        private const val TAG = "DaoViewModel"
    }

    var uiState by mutableStateOf(CahierUiState())
        private set

    private val _currentNoteId = MutableStateFlow(0L)
    val currentNoteId: StateFlow<Long> = _currentNoteId.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val note: StateFlow<CahierUiState> = currentNoteId.flatMapLatest { id ->
        notesRepository.getNoteStream(id)
            .filterNotNull()
            .map { CahierUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CahierUiState()
            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CahierUiState()
    )

    fun setCurrentNoteId(noteId: Long) {
        _currentNoteId.update {
            noteId
        }
    }

    fun updateUiState(note: Note) {
        uiState = CahierUiState(note)
    }

    suspend fun updateNote() {
        notesRepository.updateNote(uiState.note)
    }

    suspend fun addNote() {
        notesRepository.addNote(uiState.note)
    }

    suspend fun deleteNote() {
        notesRepository.deleteNote(note.value.note)
    }

    fun resetUiState() {
        val newNote = Note(id = 0, title = "", text = "", image = null)
        uiState = CahierUiState(newNote)
    }
}