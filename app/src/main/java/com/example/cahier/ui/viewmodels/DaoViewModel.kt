package com.example.cahier.ui.viewmodels

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DaoViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {
    //    var uiState by mutableStateOf(CahierUiState())
//        private set
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
//        notesRepository.getNoteStream(currentNoteId.value)
//            .filterNotNull()
//            .map { CahierUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = CahierUiState()
//            )
//    fun setCurrentNoteId(noteId: Long) {
//        _currentNoteId.value = noteId
//    }

    fun setCurrentNoteId(noteId: Long) {
        _currentNoteId.update {
           noteId
        }
    }

    /**
     * Updates the [uiState] with the note values provided in the argument. This method also triggers
     * a validation for input values.
     */
//    fun updateUiState(note: Note) {
//        _uiState.update {
//            it.copy(note = note)
//        }
//    }
    fun updateUiState(note: Note){
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
//    fun addNote() {
//        viewModelScope.launch {
//            notesRepository.addNote(uiState.value.note)
//        }
//    }

//    fun getNote() {
//        viewModelScope.launch {
//            notesRepository.getNoteStream(currentNoteId.value)
//        }
//    }

//fun deleteNote() {
//    viewModelScope.launch {
//        notesRepository.deleteNote(uiState.value.note)
//    }
//}

//    fun updateNote() {
//        viewModelScope.launch {
//            notesRepository.updateNote(uiState.value.note)
//        }
//    }

    /**
     * Resets the canvas text fields
     */
//    fun resetUiState() {
//       val newNote = Note(id = 0, title = "", text = "", image = null)
//        uiState = CahierUiState(newNote)
//    }

    fun resetUiState() {
        val newNote = Note(id = 0, title = "", text = "", image = null)
        uiState = CahierUiState(newNote)
    }
}