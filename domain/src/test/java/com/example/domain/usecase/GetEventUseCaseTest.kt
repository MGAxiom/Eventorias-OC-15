package com.example.domain.usecase

import com.example.domain.model.Evento
import com.example.domain.repository.FirestoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetEventUseCaseTest {

    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var getEventUseCase: GetEventUseCase

    @Before
    fun setup() {
        firestoreRepository = mockk()
        getEventUseCase = GetEventUseCase(firestoreRepository)
    }

    @Test
    fun `invoke should return event when found`() = runTest {
        val eventId = "event123"
        val evento = Evento(
            id = eventId,
            name = "Test Event",
            description = "Test Description"
        )
        coEvery { firestoreRepository.getEvent(eventId) } returns Result.success(evento)

        val result = getEventUseCase(eventId)

        assertTrue(result.isSuccess)
        assertEquals(evento, result.getOrNull())
        coVerify(exactly = 1) { firestoreRepository.getEvent(eventId) }
    }

    @Test
    fun `invoke should return failure when event not found`() = runTest {
        val eventId = "nonexistent"
        val exception = Exception("Event not found")
        coEvery { firestoreRepository.getEvent(eventId) } returns Result.failure(exception)

        val result = getEventUseCase(eventId)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { firestoreRepository.getEvent(eventId) }
    }
}
