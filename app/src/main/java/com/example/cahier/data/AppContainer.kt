package com.example.cahier.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val noteRepository: NoteRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineNoteRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [NoteRepository]
     */
    override val noteRepository: NoteRepository by lazy {
        OfflineNoteRepository(NoteDatabase.getDatabase(context).noteDao())
    }
}
