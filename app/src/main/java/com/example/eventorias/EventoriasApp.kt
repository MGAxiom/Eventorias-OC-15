package com.example.eventorias

import android.app.Application
import com.example.data.di.dataModule
import com.example.eventorias.di.appModule
import com.example.eventorias.di.useCaseModule
import com.example.data.di.firebaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EventoriasApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@EventoriasApp)
            modules(appModule, firebaseModule, useCaseModule, dataModule)
        }
    }
}
