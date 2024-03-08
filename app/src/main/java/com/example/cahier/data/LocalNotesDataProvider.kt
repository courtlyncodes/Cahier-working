package com.example.cahier.data

import java.time.LocalDate


object LocalNotesDataProvider {
    val allNotes = listOf (
        Note(
            id =  1,
            title = "Note 1",
            date = LocalDate.of(2020, 6, 15)
        ),
        Note(
            id =  2,
            title = "Note 2",
            date = LocalDate.of(2020, 8,  1)
        ),
        Note(
            id =  3,
            title = "Note 3",
            date = LocalDate.of(2020, 8, 7)
        ),
        Note(
            id =  4,
            title = "Note 4",
            date = LocalDate.of(2020, 8, 19)
        ),
        Note(
            id =  5,
            title = "Note 5",
            date = LocalDate.of(2020, 8, 28)
        ),
        Note(
            id =  6,
            title = "Note 6",
            date = LocalDate.of(2020, 8, 16)
        ),
        Note(
            id =  7,
            title = "Note 7",
            date = LocalDate.of(2020, 12, 30)
        ),
        Note(
            id =  8,
            title = "Note 8",
            date = LocalDate.of(2021, 3, 12)
        ),
        Note(
            id =  9,
            title = "Note 9",
            date = LocalDate.of(2021, 7, 31)
        )
    )
}