package com.example.eventorias.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.model.Evento
import com.example.domain.model.User
import com.example.domain.usecase.AddEventUseCase
import com.example.domain.usecase.GetAllEventsUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetEventUseCase
import com.example.domain.usecase.GetMapUrlUseCase
import com.example.domain.usecase.UploadImageUseCase
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAllEventsUseCase: GetAllEventsUseCase
    private lateinit var getEventUseCase: GetEventUseCase
    private lateinit var addEventUseCase: AddEventUseCase
    private lateinit var uploadImageUseCase: UploadImageUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getMapUrlUseCase: GetMapUrlUseCase
    private lateinit var viewModel: EventViewModelImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllEventsUseCase = mockk()
        getEventUseCase = mockk()
        addEventUseCase = mockk()
        uploadImageUseCase = mockk()
        getCurrentUserUseCase = mockk()
        getMapUrlUseCase = mockk()

        every { getAllEventsUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): EventViewModelImpl {
        return EventViewModelImpl(
            getAllEventsUseCase = getAllEventsUseCase,
            getEventUseCase = getEventUseCase,
            addEventUseCase = addEventUseCase,
            uploadImageUseCase = uploadImageUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            getMapUrlUseCase = getMapUrlUseCase
        )
    }

    @Test
    fun `initial state should be Success with empty list`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Success)
            assertEquals(0, (state as EventUiState.Success).events.size)
        }
    }

    @Test
    fun `onAction TitleChanged should update event title and validate`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.TitleChanged("New Event Title"))
        advanceUntilIdle()

        viewModel.event.test {
            val event = awaitItem()
            assertEquals("New Event Title", event.name)
        }

        viewModel.validationErrors.test {
            val errors = awaitItem()
            assertFalse(errors.containsKey("title"))
        }
    }

    @Test
    fun `onAction TitleChanged with empty string should show validation error`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.TitleChanged(""))
        advanceUntilIdle()

        viewModel.validationErrors.test {
            val errors = awaitItem()
            assertTrue(errors.containsKey("title"))
            assertEquals("Title cannot be empty", errors["title"])
        }
    }

    @Test
    fun `onAction DescriptionChanged should update event description`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.DescriptionChanged("Event Description"))
        advanceUntilIdle()

        viewModel.event.test {
            val event = awaitItem()
            assertEquals("Event Description", event.description)
        }
    }

    @Test
    fun `onAction LocationChanged should update event location`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.LocationChanged("123 Main St"))
        advanceUntilIdle()

        viewModel.event.test {
            val event = awaitItem()
            assertEquals("123 Main St", event.location)
        }
    }

    @Test
    fun `addEvent should fail validation when title is empty`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val currentUser = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns currentUser

        viewModel.addEvent()
        advanceUntilIdle()

        viewModel.validationErrors.test {
            val errors = awaitItem()
            assertTrue(errors.containsKey("title"))
            assertTrue(errors.containsKey("description"))
            assertTrue(errors.containsKey("address"))
        }

        coVerify(exactly = 0) { addEventUseCase(any()) }
    }

    @Test
    fun `addEvent should succeed when all fields are valid`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val currentUser = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns currentUser

        val evento = Evento(
            name = "Test Event",
            description = "Test Description",
            location = "Test Location"
        )

        coEvery { addEventUseCase(any()) } returns Result.success(Unit)
        every { getAllEventsUseCase() } returns flowOf(listOf(evento))

        viewModel.onAction(FormEvent.TitleChanged("Test Event"))
        viewModel.onAction(FormEvent.DescriptionChanged("Test Description"))
        viewModel.onAction(FormEvent.LocationChanged("Test Location"))
        advanceUntilIdle()

        viewModel.addEvent()
        advanceUntilIdle()

        viewModel.eventSaved.test {
            assertTrue(awaitItem())
        }

        coVerify { addEventUseCase(any()) }
    }

    @Test
    fun `addEvent should show error when user is not logged in`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        every { getCurrentUserUseCase() } returns null

        viewModel.onAction(FormEvent.TitleChanged("Test Event"))
        viewModel.onAction(FormEvent.DescriptionChanged("Test Description"))
        viewModel.onAction(FormEvent.LocationChanged("Test Location"))
        advanceUntilIdle()

        viewModel.addEvent()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Error)
            assertEquals("You must be logged in to create an event.", (state as EventUiState.Error).message)
        }
    }

    @Test
    fun `getAllEvents should update uiState with events`() = runTest {
        val events = listOf(
            Evento(id = "1", name = "Event 1"),
            Evento(id = "2", name = "Event 2")
        )
        every { getAllEventsUseCase() } returns flowOf(events)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Success)
            assertEquals(2, (state as EventUiState.Success).events.size)
        }
    }
}
