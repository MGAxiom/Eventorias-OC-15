package com.example.domain.usecase

import com.example.domain.repository.NotificationPreferenceRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationUseCaseTest {

    private val repository: NotificationPreferenceRepository = mockk()
    private val setNotificationsEnabledUseCase = SetNotificationsEnabledUseCase(repository)
    private val areNotificationsEnabledUseCase = AreNotificationsEnabledUseCase(repository)

    @Test
    fun `SetNotificationsEnabledUseCase should call repository`() = runTest {
        val enabled = true
        coEvery { repository.setNotificationsEnabled(enabled) } returns Result.success(Unit)

        val result = setNotificationsEnabledUseCase(enabled)

        assertEquals(Result.success(Unit), result)
    }

    @Test
    fun `AreNotificationsEnabledUseCase should return flow from repository`() = runTest {
        val enabledFlow = flowOf(true)
        every { repository.areNotificationsEnabled() } returns enabledFlow

        val result = areNotificationsEnabledUseCase()

        assertEquals(enabledFlow, result)
    }
}
