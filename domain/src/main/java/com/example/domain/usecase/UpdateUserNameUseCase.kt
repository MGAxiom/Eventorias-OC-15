package com.example.domain.usecase

import com.example.domain.repository.AuthRepository

class UpdateUserNameUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userName: String): Result<String> {
        return try {

            authRepository.updateUserProfile(displayName = userName)
                .onSuccess {
                    return Result.success(userName)
                }
                .onFailure { exception ->
                    return Result.failure(exception)
                }

            Result.success(userName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}