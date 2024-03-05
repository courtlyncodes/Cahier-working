package com.example.cahier.data

import java.time.LocalDate


object LocalNotesDataProvider {
    val allNotes = listOf (
        Note(
            title = "Note 1",
            date = LocalDate.of(2020, 6, 15)
        ),
        Note(
            title = "Note 2",
            date = LocalDate.of(2020, 8,  1)
        ),
        Note(
            title = "Note 3",
            date = LocalDate.of(2020, 8, 7)
        ),
        Note(
            title = "Note 4",
            date = LocalDate.of(2020, 8, 19)
        ),
        Note(
            title = "Note 5",
            date = LocalDate.of(2020, 8, 28)
        ),
        Note(
            title = "Note 6",
            date = LocalDate.of(2020, 8, 16)
        ),
        Note(
            title = "Note 7",
            date = LocalDate.of(2020, 12, 30)
        ),
        Note(
            title = "Note 8",
            date = LocalDate.of(2021, 3, 12)
        ),
        Note(
            title = "Note 9",
            date = LocalDate.of(2021, 7, 31)
        )
    )
}