package com.example.data.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.FirestoreRepositoryImpl
import com.example.data.repository.GoogleMapStaticRepositoryImpl
import com.example.data.repository.ImageRepositoryImpl
import com.example.data.repository.NotificationPreferencesRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.FirestoreRepository
import com.example.domain.repository.GoogleMapStaticRepository
import com.example.domain.repository.ImageRepository
import com.example.domain.repository.NotificationPreferenceRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get()
        )
    }

    single<ImageRepository> {
        ImageRepositoryImpl(
            context = get(),
            storage = get()
        )
    }

    single<GoogleMapStaticRepository> {
        GoogleMapStaticRepositoryImpl(
            apiKey = get()
        )
    }
    
    single<NotificationPreferenceRepository> {
        NotificationPreferencesRepositoryImpl(
            context = get(),
            firebaseMessaging = get()
        )
    }

    single<FirestoreRepository> {
        FirestoreRepositoryImpl(
            firestore = get()
        )
    }
}
