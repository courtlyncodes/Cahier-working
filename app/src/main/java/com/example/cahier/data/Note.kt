package com.example.cahier.data

import java.time.LocalDate


data class Note(
    val id: Int?,
    val title: String,
//    val description: String?,
    val date: LocalDate,
//    val image: Painter? /*(painter? int?)*/
)