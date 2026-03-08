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
import org.junit.Assert.assertNull
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
    fun `onAction DescriptionChanged with empty string should show validation error`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.DescriptionChanged(""))
        advanceUntilIdle()

        viewModel.validationErrors.test {
            val errors = awaitItem()
            assertTrue(errors.containsKey("description"))
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
    fun `onAction LocationChanged with empty string should show validation error`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.LocationChanged(""))
        advanceUntilIdle()

        viewModel.validationErrors.test {
            val errors = awaitItem()
            assertTrue(errors.containsKey("address"))
        }
    }

    @Test
    fun `onAction DateChanged should update event date`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val newDate = "25/12/2023"
        viewModel.onAction(FormEvent.DateChanged(newDate))
        advanceUntilIdle()

        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event.date != 0L)
        }
    }

    @Test
    fun `onAction TimeChanged should update event time`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val newTime = "14:30"
        viewModel.onAction(FormEvent.TimeChanged(newTime))
        advanceUntilIdle()

        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event.date != 0L)
        }
    }

    @Test
    fun `addEvent should fail validation when fields are empty`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

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

        coEvery { addEventUseCase(any()) } returns Result.success(Unit)
        every { getAllEventsUseCase() } returns flowOf(emptyList())

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
    fun `addEvent should upload image if photoUri is present`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val currentUser = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns currentUser
        coEvery { uploadImageUseCase("uri", "user1") } returns "http://photo.url"
        coEvery { addEventUseCase(any()) } returns Result.success(Unit)

        viewModel.onAction(FormEvent.TitleChanged("Test Event"))
        viewModel.onAction(FormEvent.DescriptionChanged("Test Description"))
        viewModel.onAction(FormEvent.LocationChanged("Test Location"))
        viewModel.onAction(FormEvent.PhotoUriChanged("uri"))
        advanceUntilIdle()

        viewModel.addEvent()
        advanceUntilIdle()

        coVerify { uploadImageUseCase("uri", "user1") }
        coVerify { addEventUseCase(match { it.photoUrl == "http://photo.url" }) }
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
    fun `addEvent should show error when addEventUseCase fails`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val currentUser = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns currentUser
        coEvery { addEventUseCase(any()) } returns Result.failure(Exception("Firestore error"))

        viewModel.onAction(FormEvent.TitleChanged("Test Event"))
        viewModel.onAction(FormEvent.DescriptionChanged("Test Description"))
        viewModel.onAction(FormEvent.LocationChanged("Test Location"))
        advanceUntilIdle()

        viewModel.addEvent()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Error)
            assertEquals("Firestore error", (state as EventUiState.Error).message)
        }
    }

    @Test
    fun `addEvent should handle exception during execution`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        val currentUser = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns currentUser
        coEvery { addEventUseCase(any()) } throws RuntimeException("Unexpected error")

        viewModel.onAction(FormEvent.TitleChanged("Test Event"))
        viewModel.onAction(FormEvent.DescriptionChanged("Test Description"))
        viewModel.onAction(FormEvent.LocationChanged("Test Location"))
        advanceUntilIdle()

        viewModel.addEvent()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Error)
            assertEquals("Unexpected error", (state as EventUiState.Error).message)
        }
    }

    @Test
    fun `getEventById should update selectedEvent on success`() = runTest {
        val event = Evento(id = "1", name = "Event 1")
        coEvery { getEventUseCase("1") } returns Result.success(event)
        
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.getEventById("1")
        advanceUntilIdle()

        viewModel.selectedEvent.test {
            assertEquals(event, awaitItem())
        }
    }

    @Test
    fun `getEventById should show error on failure`() = runTest {
        coEvery { getEventUseCase("1") } returns Result.failure(Exception("Not found"))
        
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.getEventById("1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is EventUiState.Error)
            assertEquals("Not found", (state as EventUiState.Error).message)
        }
    }

    @Test
    fun `getMapUrl should call use case`() {
        val address = "test address"
        every { getMapUrlUseCase(address) } returns "http://map.url"
        
        viewModel = createViewModel()
        val result = viewModel.getMapUrl(address)
        
        assertEquals("http://map.url", result)
        coVerify { getMapUrlUseCase(address) }
    }

    @Test
    fun `onSaveComplete should reset event and eventSaved`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(FormEvent.TitleChanged("Temp"))
        
        viewModel.onSaveComplete()
        
        viewModel.eventSaved.test {
            assertFalse(awaitItem())
        }
        viewModel.event.test {
            val event = awaitItem()
            assertEquals("", event.name)
            // It was fail because assertNull(event.id) but event.id default is "" not null
            assertEquals("", event.id)
        }
    }
}
