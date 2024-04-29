package com.example.cahier.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.CanvasUiState
import com.example.cahier.data.Note
import com.example.cahier.data.NotesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DaoViewModel(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {

    companion object { private const val TAG = "DaoViewModel" }
    /**
     * Holds current item ui state
     */
    var canvasUiState by mutableStateOf(CanvasUiState())
        private set

    private val noteId: Long = savedStateHandle.get<String>("noteId")?.toLong() ?: 0
    init {
        viewModelScope.launch {
            canvasUiState = notesRepository.getNoteStream(noteId)
                .filterNotNull()
                .first()
                .toCanvasUiState(true)!!
        }
    }
    /**
     * Updates the [canvasUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(note: Note) {
        Log.wtf(TAG, "note: $note")
        canvasUiState =
            CanvasUiState(note = note)
    }

    /**
     * Inserts an [Note] in the Room database
     */
    suspend fun addNote() {
        Log.wtf(TAG, "addNote")
        notesRepository.addNote(canvasUiState.note)
    }


    fun resetUiState() {
       val newNote = Note(id = 0, title = "", text = "", image = 0)
        canvasUiState =
            CanvasUiState(note = newNote)
    }

    private fun validateInput(uiState: Note = canvasUiState.note): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }


}

fun Note.toNote(): Note = Note(
    id = id,
    title = title,
    text = text,
    image = image
)



/**
 * Extension function to convert [Note] to [CanvasUiState]
 */
fun Note.toCanvasUiState(isEntryValid: Boolean = false): CanvasUiState? = this.toNoteDetails()?.let {
    CanvasUiState(
        note = it
    )
}

/**
 * Extension function to convert [Note] to [Note]
 */
fun Note.toNoteDetails(): Note? = text?.let {
    Note(
        id = id,
        title = title,
        text = it,
        image = image
    )
}