package com.example.domain.usecase

import app.cash.turbine.test
import com.example.domain.model.Evento
import com.example.domain.repository.FirestoreRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllEventsUseCaseTest {

    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var getAllEventsUseCase: GetAllEventsUseCase

    @Before
    fun setup() {
        firestoreRepository = mockk()
        getAllEventsUseCase = GetAllEventsUseCase(firestoreRepository)
    }

    @Test
    fun `invoke should return flow of events from repository`() = runTest {
        // Given
        val events = listOf(
            Evento(id = "1", name = "Event 1"),
            Evento(id = "2", name = "Event 2"),
            Evento(id = "3", name = "Event 3")
        )
        every { firestoreRepository.getAllEvents() } returns flowOf(events)

        // When
        val result = getAllEventsUseCase()

        // Then
        result.test {
            val emittedEvents = awaitItem()
            assertEquals(3, emittedEvents.size)
            assertEquals(events, emittedEvents)
            awaitComplete()
        }
        verify(exactly = 1) { firestoreRepository.getAllEvents() }
    }

    @Test
    fun `invoke should return empty list when repository returns empty flow`() = runTest {
        // Given
        every { firestoreRepository.getAllEvents() } returns flowOf(emptyList())

        // When
        val result = getAllEventsUseCase()

        // Then
        result.test {
            val emittedEvents = awaitItem()
            assertEquals(0, emittedEvents.size)
            awaitComplete()
        }
        verify(exactly = 1) { firestoreRepository.getAllEvents() }
    }
}
