package com.example.eventorias.di

import com.example.eventorias.BuildConfig
import com.example.eventorias.ui.viewmodel.EventViewModel
import com.example.eventorias.ui.viewmodel.EventViewModelImpl
import com.example.network.data.repository.GoogleMapsStaticRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { GoogleMapsStaticRepository(apiKey = BuildConfig.GOOGLE_API_KEY) }

    // ViewModels
    viewModelOf(::EventViewModelImpl) { bind<EventViewModel>() }
}
