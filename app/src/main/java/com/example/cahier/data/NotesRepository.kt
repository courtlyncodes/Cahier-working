package com.example.cahier.data

import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    /**
     * Retrieve all the notes from the given data source.
     */
    fun getAllNotesStream(): Flow<List<Note>>
    /**
     * Retrieve an note from the given data source that matches with the [id].
     */
    fun getNoteStream(id: Long): Flow<Note>
    /**
     * Insert note in the data source
     */
    suspend fun addNote(note: Note): Long
    /**
     * Delete note from the data source
     */
    suspend fun deleteNote(note: Note)
    /**
     * Update note in the data source
     */
    suspend fun updateNote(note: Note)
    
    /* Drawing functions for the stylus */
    suspend fun getDrawingsForNote(noteId: Long): Flow<List<Path>>
    suspend fun addDrawing(noteId: Long, drawing: Path): Long
    suspend fun updateDrawing(noteId: Long, drawing: Path)
    suspend fun deleteDrawing(noteId: Long, drawing: Path)
}