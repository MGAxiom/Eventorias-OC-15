package com.example.eventorias.di

import com.example.eventorias.BuildConfig
import com.example.eventorias.ui.viewmodel.EventViewModel
import com.example.network.data.repository.GoogleMapsStaticRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { GoogleMapsStaticRepository(apiKey = BuildConfig.GOOGLE_API_KEY) }
    
    viewModel { EventViewModel() }
}
