package com.example.domain.usecase

import com.example.domain.repository.NotificationPreferenceRepository
import kotlinx.coroutines.flow.Flow


class SetNotificationsEnabledUseCase(
    private val repository: NotificationPreferenceRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return repository.setNotificationsEnabled(enabled)
    }
}

class AreNotificationsEnabledUseCase(
    private val repository: NotificationPreferenceRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.areNotificationsEnabled()
    }
}