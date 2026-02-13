package com.example.domain.usecase

import com.example.domain.model.Evento
import com.example.domain.model.User
import com.example.domain.repository.FirestoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddEventUseCaseTest {

    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var addEventUseCase: AddEventUseCase

    @Before
    fun setup() {
        firestoreRepository = mockk()
        addEventUseCase = AddEventUseCase(firestoreRepository)
    }

    @Test
    fun `invoke should return success when repository adds event successfully`() = runTest {
        val evento = Evento(
            id = "1",
            name = "Test Event",
            description = "Test Description",
            location = "Test Location",
            attachedUser = User(uid = "user1", displayName = "Test User")
        )
        coEvery { firestoreRepository.addEvent(evento) } returns Result.success(Unit)

        val result = addEventUseCase(evento)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { firestoreRepository.addEvent(evento) }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        val evento = Evento(name = "Test Event")
        val exception = Exception("Failed to add event")
        coEvery { firestoreRepository.addEvent(evento) } returns Result.failure(exception)

        val result = addEventUseCase(evento)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { firestoreRepository.addEvent(evento) }
    }
}
