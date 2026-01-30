package com.example.domain.repository

import com.example.domain.model.User

interface AuthRepository {
    fun getCurrentUser(): User?
    fun signOut()
    fun deleteCurrentUserAccount(onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}