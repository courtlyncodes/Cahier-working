package com.example.cahier.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Commented columns are for future implementation and use
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
//    @ColumnInfo(name = "last_modified")
//    val lastModified: LocalDate = LocalDate.now(),
//    val tags: List<String>?,
    @ColumnInfo(name = "text")
    val text: String?,
    @ColumnInfo(name = "image")
    /*@DrawableRes */val image: Int?,
//    val list: List<Any>? = null,
//    val sketch: Painter? = null,
//    @ColumnInfo(name = "calendar_date")
//    val calendarDate: LocalDate? = null
)
//{
//    companion object {
//        val Saver: Saver<Note?, Int> = Saver(
//            { it?.id },
//            { Note(it, "", LocalDate.now(), null, null, null, null, null, null) }
//        )
//    }
//}

