package com.example.data.repository

import com.example.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ImageRepositoryImpl(private val storage: FirebaseStorage) : ImageRepository {

    override suspend fun uploadImage(imageUri: String, userId: String): String {
        val fileName = "event_images/${userId}/${UUID.randomUUID()}"
        val imageRef = storage.reference.child(fileName)
        imageRef.putFile(android.net.Uri.parse(imageUri)).await()
        return imageRef.downloadUrl.await().toString()
    }
}