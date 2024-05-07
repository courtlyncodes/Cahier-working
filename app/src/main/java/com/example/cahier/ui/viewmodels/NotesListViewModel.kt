package com.example.cahier.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotesListViewModel(notesRepository: NotesRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [NotesRepository] and mapped to
     * [NoteListUiState]
     */
    val noteList: StateFlow<NoteListUiState> =
        notesRepository.getAllNotesStream().map { NoteListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class NoteListUiState(val noteList: List<Note> = listOf())