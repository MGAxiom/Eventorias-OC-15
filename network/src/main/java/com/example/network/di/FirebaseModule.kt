package com.example.network.di

import com.example.network.data.repository.FirestoreRepository
import com.example.network.data.repository.FirestoreRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseFirestore.getInstance() }

    singleOf(::FirestoreRepositoryImpl) { bind<FirestoreRepository>() }
}
