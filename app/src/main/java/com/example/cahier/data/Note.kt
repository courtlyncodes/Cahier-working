package com.example.cahier.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.painter.Painter
import java.time.LocalDate


data class Note(
    val id: Int?,
    val title: String,
    val lastModified: LocalDate,
    val tags: List<String>?,
    val text: String?,
    @DrawableRes val image: Int?,/*(int?, url? ImageVector?)*/
    val list: List<Any>?,
    val sketch: Painter?,
    val calendarDate: LocalDate?
)
//{
//    companion object {
//        val Saver: Saver<Note?, Int> = Saver(
//            { it?.id },
//            { Note(it, "", LocalDate.now(), null, null, null, null, null, null) }
//        )
//    }
//}
