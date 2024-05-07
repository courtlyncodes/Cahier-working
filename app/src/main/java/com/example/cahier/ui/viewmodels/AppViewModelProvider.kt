package com.example.cahier.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cahier.CahierApplication

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            DaoViewModel(cahierApplication().container.notesRepository)
        }

        initializer {
            NotesListViewModel(cahierApplication().container.notesRepository)
        }
        initializer {
            DetailViewModel(
                this.createSavedStateHandle(),
                cahierApplication().container.notesRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CahierApplication].
 */
fun CreationExtras.cahierApplication(): CahierApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CahierApplication)
