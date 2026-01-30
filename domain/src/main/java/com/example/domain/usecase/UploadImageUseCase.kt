package com.example.domain.usecase

import com.example.domain.repository.ImageRepository

class UploadImageUseCase(private val imageRepository: ImageRepository) {
    suspend operator fun invoke(imageUri: String, userId: String): String {
        return imageRepository.uploadImage(imageUri, userId)
    }
}