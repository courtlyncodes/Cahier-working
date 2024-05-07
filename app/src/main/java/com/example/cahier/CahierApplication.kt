package com.example.cahier

import android.app.Application
import com.example.cahier.data.AppContainer
import com.example.cahier.data.AppDataContainer

class CahierApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}