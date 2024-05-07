package com.example.cahier.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CahierUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    var notesUiState by mutableStateOf(NotesUiState())
        private set

    private val noteId: Long = checkNotNull(savedStateHandle.get<Long>("noteId"))

    init {
        viewModelScope.launch {
            notesUiState = notesRepository.getNoteStream(noteId)
                .filterNotNull()
                .first()
                .toNotesUiState()
        }
    }

//    suspend fun updateNote() {
//        viewModelScope.launch {
//            notesRepository.updateNote()
//        }
//    }
}

data class NotesUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isEntryValid: Boolean = false
)

data class NoteDetails(
    val id: Long = 0,
    val title: String = "",
    val image: Int? = null,
    val text: String = ""
)

fun Note.toNotesUiState(): NotesUiState = NotesUiState(
    noteDetails = this.toNoteDetails()
)


fun Note.toNoteDetails(): NoteDetails =
    NoteDetails(
        id = id,
        title = title,
        text = text.toString(),
        image = image
    )

fun NoteDetails.toNote(): Note = Note (
    id = id,
    title = title,
    text = text,
    image = image

)