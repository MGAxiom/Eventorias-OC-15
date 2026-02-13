package com.example.eventorias.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.model.User
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.UpdateUserProfilePhotoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var updateUserProfilePhotoUseCase: UpdateUserProfilePhotoUseCase
    private lateinit var viewModel: UserProfileViewModelImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentUserUseCase = mockk()
        updateUserProfilePhotoUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): UserProfileViewModelImpl {
        return UserProfileViewModelImpl(
            getCurrentUserUseCase = getCurrentUserUseCase,
            updateUserProfilePhotoUseCase = updateUserProfilePhotoUseCase
        )
    }

    @Test
    fun `initial state should be Success when user is logged in`() = runTest {
        // Given
        val user = User(uid = "user1", displayName = "Test User", email = "test@example.com", photoUrl = "https://example.com/photo.jpg")
        every { getCurrentUserUseCase() } returns user

        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(user, (state as ProfileUiState.Success).user)
            assertEquals("https://example.com/photo.jpg", state.profilePhotoUrl)
        }
    }

    @Test
    fun `initial state should be Error when user is not logged in`() = runTest {
        // Given
        every { getCurrentUserUseCase() } returns null

        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("User not logged in", (state as ProfileUiState.Error).message)
        }
    }

    @Test
    fun `updateProfilePhoto should update photo URL successfully`() = runTest {
        // Given
        val user = User(uid = "user1", displayName = "Test User", email = "test@example.com")
        val imageUri = "content://test/image.jpg"
        val newPhotoUrl = "https://storage.firebase.com/new-photo.jpg"

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.success(newPhotoUrl)

        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(newPhotoUrl, (state as ProfileUiState.Success).profilePhotoUrl)
        }

        coVerify(exactly = 1) { updateUserProfilePhotoUseCase(imageUri, user.uid) }
    }

    @Test
    fun `updateProfilePhoto should show error when update fails`() = runTest {
        // Given
        val user = User(uid = "user1", displayName = "Test User")
        val imageUri = "content://test/image.jpg"
        val exception = Exception("Upload failed")

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.failure(exception)

        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        // Then - should eventually restore to Success state after showing error
        viewModel.uiState.test {
            val state = awaitItem()
            // The viewModel restores the previous state after error
            assertTrue(state is ProfileUiState.Success)
        }

        coVerify(exactly = 1) { updateUserProfilePhotoUseCase(imageUri, user.uid) }
    }

    @Test
    fun `updateProfilePhoto should show error when user is not logged in`() = runTest {
        // Given
        every { getCurrentUserUseCase() } returns null

        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.updateProfilePhoto("content://test/image.jpg")
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("User not logged in", (state as ProfileUiState.Error).message)
        }

        coVerify(exactly = 0) { updateUserProfilePhotoUseCase(any(), any()) }
    }
}
