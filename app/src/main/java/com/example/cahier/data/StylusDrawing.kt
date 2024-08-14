package com.example.cahier.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Table for stylus drawings
@Entity(
    tableName = "stylus_drawings",
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

// Data class representing a stylus drawing
data class StylusDrawing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long,
    val pathData: String
)