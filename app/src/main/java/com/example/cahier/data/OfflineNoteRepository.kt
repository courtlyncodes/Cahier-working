package com.example.cahier.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineNotesRepository(private val notesDao: NoteDao) : NotesRepository {

    override fun getAllNotesStream(): Flow<List<Note>> = notesDao.getAllNotes()

    override fun getNoteStream(id: Long): Flow<Note> = notesDao.getNote(id)

    override suspend fun addNote(note: Note): Long {
        return notesDao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    override suspend fun updateNote(note: Note) = notesDao.updateNote(note)
}