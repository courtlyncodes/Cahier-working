package com.example.cahier.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
data class StylusDrawing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long,
    val pathData: String
)