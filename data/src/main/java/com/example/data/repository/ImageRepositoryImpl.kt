package com.example.data.repository

import android.content.Context
import com.example.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import androidx.core.net.toUri

class ImageRepositoryImpl(
    private val context: Context,
    private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun uploadImage(imageUri: String, userId: String): String {
        val uri = imageUri.toUri()

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Unable to open InputStream for URI: $uri")

        val fileName = "event_images/$userId/${UUID.randomUUID()}.jpg"
        val imageRef = storage.reference.child(fileName)

        imageRef.putStream(inputStream).await()
        return imageRef.downloadUrl.await().toString()
    }
}
