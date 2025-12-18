package com.example.eventorias.di

import com.example.eventorias.BuildConfig
import com.example.eventorias.ui.viewmodel.EventCreationViewModel
import com.example.network.GoogleMapsStaticRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { GoogleMapsStaticRepository(apiKey = BuildConfig.GOOGLE_API_KEY) }
    
    viewModel { EventCreationViewModel() }
}
