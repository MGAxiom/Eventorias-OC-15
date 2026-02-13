package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ImageRepository

class UpdateUserProfilePhotoUseCase(
    private val imageRepository: ImageRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(imageUri: String, userId: String): Result<String> {
        return try {
            // Upload image to Firebase Storage
            val photoUrl = imageRepository.uploadImage(imageUri, userId)

            // Update Firebase Auth user profile
            authRepository.updateUserProfile(photoUrl = photoUrl)
                .onSuccess {
                    return Result.success(photoUrl)
                }
                .onFailure { exception ->
                    return Result.failure(exception)
                }

            Result.success(photoUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
