package com.example.cahier.data

import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val notesDao: NoteDao): NoteRepository {
    companion object { private const val TAG = "OfflineNotesRepository" }
    override fun getAllNotesStream(): Flow<List<Note>> = notesDao.getAllNotes()

    override fun getNoteStream(id: Long): Flow<Note> = notesDao.getNote(id)

    override suspend fun addNote(note: Note): Long {
        return notesDao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    override suspend fun updateNote(note: Note) = notesDao.updateNote(note)
}