package com.example.eventorias.di

import com.example.eventorias.BuildConfig
import com.example.eventorias.ui.viewmodel.EventViewModel
import com.example.eventorias.ui.viewmodel.EventViewModelImpl
import com.example.eventorias.ui.viewmodel.UserProfileViewModel
import com.example.eventorias.ui.viewmodel.UserProfileViewModelImpl
import com.example.data.repository.GoogleMapStaticRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { BuildConfig.GOOGLE_API_KEY }

    // ViewModels
    viewModel<EventViewModel> {
        EventViewModelImpl(
            getAllEventsUseCase = get(),
            addEventUseCase = get(),
            uploadImageUseCase = get(),
            getCurrentUserUseCase = get(),
            getMapUrlUseCase = get(),
            getEventUseCase = get()
        )
    }

    viewModel<UserProfileViewModel> {
        UserProfileViewModelImpl(
            getCurrentUserUseCase = get(),
            updateUserProfilePhotoUseCase = get(),
            updateUserNameUseCase = get()
        )
    }
}
