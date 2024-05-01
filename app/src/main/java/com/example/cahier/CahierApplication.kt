package com.example.cahier

import android.app.Application
import com.example.cahier.data.AppContainer
import com.example.cahier.data.AppDataContainer
import com.example.cahier.data.NoteDatabase

class CahierApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    val database: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
}