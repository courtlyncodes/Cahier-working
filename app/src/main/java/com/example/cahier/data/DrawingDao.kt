package com.example.cahier.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface DrawingDao {
    @Query("SELECT * FROM stylus_drawings WHERE noteId = :noteId")
    fun getDrawingsForNote(noteId: Long): Flow<List<StylusDrawing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDrawing(drawing: StylusDrawing): Long

    @Update
    suspend fun updateDrawing(drawing: StylusDrawing)

    @Delete
    suspend fun deleteDrawing(drawing: StylusDrawing)
}