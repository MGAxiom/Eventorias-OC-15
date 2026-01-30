package com.example.data.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.GoogleMapStaticRepositoryImpl
import com.example.data.repository.ImageRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.GoogleMapStaticRepository
import com.example.domain.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }

    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get()
        )
    }

    single<ImageRepository> {
        ImageRepositoryImpl(
            storage = get()
        )
    }

    single<GoogleMapStaticRepository> {
        GoogleMapStaticRepositoryImpl(
            apiKey = get()
        )
    }
}
