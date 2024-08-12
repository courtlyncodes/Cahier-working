package com.example.cahier.data

import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineNotesRepository(private val notesDao: NoteDao, private val drawingDao: DrawingDao) : NotesRepository {

    override fun getAllNotesStream(): Flow<List<Note>> = notesDao.getAllNotes()

    override fun getNoteStream(id: Long): Flow<Note> = notesDao.getNote(id)

    override suspend fun addNote(note: Note): Long {
        return notesDao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    override suspend fun updateNote(note: Note) = notesDao.updateNote(note)

    override suspend fun getDrawingsForNote(noteId: Long): Flow<List<Path>> {
        return drawingDao.getDrawingsForNote(noteId).map { drawings ->
            drawings.map { drawing ->
                Converters().toPath(drawing.pathData) ?: Path()
            }
        }
    }

    override suspend fun addDrawing(noteId: Long, drawing: Path): Long {
        val pathString = Converters().fromPath(drawing)
        val stylusDrawing = StylusDrawing(noteId = noteId, pathData = pathString ?: "")
        return drawingDao.addDrawing(stylusDrawing)
    }

    override suspend fun updateDrawing(noteId: Long, drawing: Path) {
        val pathString = Converters().fromPath(drawing)
        val stylusDrawing= StylusDrawing(noteId = noteId, pathData = pathString ?: "")
        drawingDao.updateDrawing(stylusDrawing)
    }

    override suspend fun deleteDrawing(noteId: Long, drawing: Path) {
        val pathString = Converters().fromPath(drawing)
        val stylusDrawing = StylusDrawing(noteId = noteId, pathData = pathString ?: "")
        drawingDao.deleteDrawing(stylusDrawing)
    }
}