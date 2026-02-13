package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateUserProfilePhotoUseCaseTest {

    private lateinit var imageRepository: ImageRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var updateUserProfilePhotoUseCase: UpdateUserProfilePhotoUseCase

    @Before
    fun setup() {
        imageRepository = mockk()
        authRepository = mockk()
        updateUserProfilePhotoUseCase = UpdateUserProfilePhotoUseCase(imageRepository, authRepository)
    }

    @Test
    fun `invoke should upload image and update auth profile successfully`() = runTest {
        // Given
        val imageUri = "content://test/image.jpg"
        val userId = "user123"
        val photoUrl = "https://storage.firebase.com/image.jpg"

        coEvery { imageRepository.uploadImage(imageUri, userId) } returns photoUrl
        coEvery { authRepository.updateUserProfile(photoUrl = photoUrl) } returns Result.success(Unit)

        // When
        val result = updateUserProfilePhotoUseCase(imageUri, userId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(photoUrl, result.getOrNull())
        coVerify(exactly = 1) { imageRepository.uploadImage(imageUri, userId) }
        coVerify(exactly = 1) { authRepository.updateUserProfile(photoUrl = photoUrl) }
    }

    @Test
    fun `invoke should return failure when image upload fails`() = runTest {
        val imageUri = "content://test/image.jpg"
        val userId = "user123"
        val exception = Exception("Upload failed")

        coEvery { imageRepository.uploadImage(imageUri, userId) } throws exception

        val result = updateUserProfilePhotoUseCase(imageUri, userId)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { imageRepository.uploadImage(imageUri, userId) }
        coVerify(exactly = 0) { authRepository.updateUserProfile(any(), any()) }
    }

    @Test
    fun `invoke should return failure when auth update fails`() = runTest {
        val imageUri = "content://test/image.jpg"
        val userId = "user123"
        val photoUrl = "https://storage.firebase.com/image.jpg"
        val exception = Exception("Auth update failed")

        coEvery { imageRepository.uploadImage(imageUri, userId) } returns photoUrl
        coEvery { authRepository.updateUserProfile(photoUrl = photoUrl) } returns Result.failure(exception)

        val result = updateUserProfilePhotoUseCase(imageUri, userId)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { imageRepository.uploadImage(imageUri, userId) }
        coVerify(exactly = 1) { authRepository.updateUserProfile(photoUrl = photoUrl) }
    }
}
