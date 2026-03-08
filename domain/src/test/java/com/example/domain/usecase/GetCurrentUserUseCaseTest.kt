package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCurrentUserUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    }

    @Test
    fun `invoke should return user from repository`() {
        // Given
        val user = User(uid = "user123", displayName = "Test User")
        every { authRepository.getCurrentUser() } returns user

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertEquals(user, result)
        verify(exactly = 1) { authRepository.getCurrentUser() }
    }

    @Test
    fun `invoke should return null when no user is logged in`() {
        // Given
        every { authRepository.getCurrentUser() } returns null

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertEquals(null, result)
        verify(exactly = 1) { authRepository.getCurrentUser() }
    }
}
