package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}