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
            CanvasScreenViewModel(
                this.createSavedStateHandle(),
                cahierApplication().container.notesRepository
            )
        }

        initializer {
            HomeScreenViewModel(cahierApplication().container.notesRepository)
        }

        // Each View Model should be initialized if it uses dependency injection without Dagger or Hilt
        initializer {
            StylusViewModel(
                cahierApplication().container.notesRepository,
            )
        }
        initializer {
            StylusViewModel(
                cahierApplication().container.notesRepository,
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
