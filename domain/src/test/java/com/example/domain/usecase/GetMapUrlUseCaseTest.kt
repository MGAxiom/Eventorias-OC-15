package com.example.domain.usecase

import com.example.domain.repository.GoogleMapStaticRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMapUrlUseCaseTest {

    private val repository: GoogleMapStaticRepository = mockk()
    private val useCase = GetMapUrlUseCase(repository)

    @Test
    fun `invoke should return map url from repository`() {
        // Given
        val address = "1600 Amphitheatre Parkway, Mountain View, CA"
        val expectedUrl = "https://maps.googleapis.com/maps/api/staticmap?center=$address"
        every { repository.getStaticMapUrl(address) } returns expectedUrl

        // When
        val result = useCase(address)

        // Then
        assertEquals(expectedUrl, result)
    }
}
