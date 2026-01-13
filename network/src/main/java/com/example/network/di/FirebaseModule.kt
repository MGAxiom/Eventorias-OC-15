package com.example.network.di

import android.system.Os.bind
import com.example.network.data.repository.FirestoreRepository
import com.example.network.data.repository.FirestoreRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.auth }
    singleOf(::FirestoreRepositoryImpl) { bind<FirestoreRepository>() }
}
