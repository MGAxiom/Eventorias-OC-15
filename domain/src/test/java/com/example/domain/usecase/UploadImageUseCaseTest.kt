package com.example.domain.usecase

import com.example.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UploadImageUseCaseTest {

    private val repository: ImageRepository = mockk()
    private val useCase = UploadImageUseCase(repository)

    @Test
    fun `invoke should return image url from repository`() = runTest {
        // Given
        val uri = "content://media/external/images/media/1"
        val userId = "user123"
        val expectedUrl = "https://firebasestorage.googleapis.com/v0/b/app/o/user123%2Fprofile.jpg"
        coEvery { repository.uploadImage(uri, userId) } returns expectedUrl

        // When
        val result = useCase(uri, userId)

        // Then
        assertEquals(expectedUrl, result)
    }
}
