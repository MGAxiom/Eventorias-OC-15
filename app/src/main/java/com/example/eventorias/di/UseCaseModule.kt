package com.example.eventorias.di

import com.example.eventorias.core.domain.usecase.AddEventUseCase
import com.example.eventorias.core.domain.usecase.GetAllEventsUseCase
import com.example.eventorias.core.domain.usecase.GetEventUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAllEventsUseCase(get()) }
    factory { AddEventUseCase(get()) }
    factory { GetEventUseCase(get()) }
}
