package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateUserNameUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val useCase = UpdateUserNameUseCase(authRepository)

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val newName = "New Name"
        coEvery { authRepository.updateUserProfile(displayName = newName) } returns Result.success(Unit)

        // When
        val result = useCase(newName)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(newName, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val newName = "New Name"
        val exception = Exception("Update failed")
        coEvery { authRepository.updateUserProfile(displayName = newName) } returns Result.failure(exception)

        // When
        val result = useCase(newName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke should return failure when repository throws`() = runTest {
        // Given
        val newName = "New Name"
        val exception = RuntimeException("Unexpected error")
        coEvery { authRepository.updateUserProfile(displayName = newName) } throws exception

        // When
        val result = useCase(newName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
