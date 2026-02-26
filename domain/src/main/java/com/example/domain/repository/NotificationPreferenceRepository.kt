package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationPreferenceRepository {
    suspend fun setNotificationsEnabled(enabled: Boolean): Result<Unit>

    fun areNotificationsEnabled(): Flow<Boolean>
}