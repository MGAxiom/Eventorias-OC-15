package com.example.eventorias.di

import com.example.domain.usecase.AddEventUseCase
import com.example.domain.usecase.GetAllEventsUseCase
import com.example.domain.usecase.GetEventUseCase
import com.example.domain.usecase.UploadImageUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetMapUrlUseCase
import com.example.domain.usecase.UpdateUserNameUseCase
import com.example.domain.usecase.UpdateUserProfilePhotoUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAllEventsUseCase(get()) }
    factory { AddEventUseCase(get()) }
    factory { GetEventUseCase(get()) }
    factory { UploadImageUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetMapUrlUseCase(get()) }
    factory { UpdateUserProfilePhotoUseCase(get(), get()) }
    factory { UpdateUserNameUseCase(get()) }
}
