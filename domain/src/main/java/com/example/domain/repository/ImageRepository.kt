package com.example.domain.repository

interface ImageRepository {
    suspend fun uploadImage(imageUri: String, userId: String): String
}