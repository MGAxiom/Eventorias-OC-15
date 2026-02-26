package com.example.eventorias.ui.model

import com.example.domain.model.User

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(
        val user: User,
        val profilePhotoUrl: String?,
        val notificationsEnabled: Boolean
    ) : ProfileUiState()

    data class Error(val message: String) : ProfileUiState()
}