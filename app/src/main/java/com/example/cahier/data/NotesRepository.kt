package com.example.cahier.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    /**
     * Retrieve all the notes from the given data source.
     */
    fun getAllNotesStream(): Flow<List<Note>>
    /**
     * Retrieve an note from the given data source that matches with the [id].
     */
    fun getNoteStream(id: Long): Flow<Note?>
    /**
     * Insert note in the data source
     */
    suspend fun addNote(note: Note)
    /**
     * Delete note from the data source
     */
    suspend fun deleteNote(note: Note)
    /**
     * Update note in the data source
     */
    suspend fun updateNote(note: Note)
}