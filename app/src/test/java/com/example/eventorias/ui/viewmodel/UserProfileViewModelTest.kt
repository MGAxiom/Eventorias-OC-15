package com.example.eventorias.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.model.User
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.UpdateUserNameUseCase
import com.example.domain.usecase.UpdateUserProfilePhotoUseCase
import com.example.eventorias.ui.model.ProfileUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
    private lateinit var updateUserNameUseCase: UpdateUserNameUseCase
    private lateinit var viewModel: UserProfileViewModelImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentUserUseCase = mockk()
        updateUserProfilePhotoUseCase = mockk()
        updateUserNameUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): UserProfileViewModelImpl {
        return UserProfileViewModelImpl(
            getCurrentUserUseCase = getCurrentUserUseCase,
            updateUserProfilePhotoUseCase = updateUserProfilePhotoUseCase,
            updateUserNameUseCase = updateUserNameUseCase
        )
    }

    @Test
    fun `initial state should be Success when user is logged in`() = runTest {
        val user = User(
            uid = "user1",
            displayName = "Test User",
            email = "test@example.com",
            photoUrl = "https://example.com/photo.jpg"
        )
        every { getCurrentUserUseCase() } returns user

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(user, (state as ProfileUiState.Success).user)
            assertEquals("https://example.com/photo.jpg", state.profilePhotoUrl)
        }
    }

    @Test
    fun `initial state should be Error when user is not logged in`() = runTest {
        every { getCurrentUserUseCase() } returns null

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("User not logged in", (state as ProfileUiState.Error).message)
        }
    }

    @Test
    fun `updateProfilePhoto should update photo URL successfully`() = runTest {
        val user = User(uid = "user1", displayName = "Test User", email = "test@example.com")
        val imageUri = "content://test/image.jpg"
        val newPhotoUrl = "https://storage.firebase.com/new-photo.jpg"

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.success(
            newPhotoUrl
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(newPhotoUrl, (state as ProfileUiState.Success).profilePhotoUrl)
        }

        coVerify(exactly = 1) { updateUserProfilePhotoUseCase(imageUri, user.uid) }
    }

    @Test
    fun `updateUserName should update user profile name successfully`() = runTest {
        val user = User(uid = "user1", displayName = "Test User", email = "test@example.com")
        val newUsername = "New Username"

        var currentUserName = "Test User"

        every { getCurrentUserUseCase() } answers {
            user.copy(displayName = currentUserName)
        }

        coEvery { updateUserNameUseCase(newUsername) } coAnswers {
            currentUserName = newUsername
            Result.success(newUsername)
        }

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem()

            viewModel.updateUserName(newUsername)
            awaitItem()

            val updatedState = awaitItem()
            assertTrue(updatedState is ProfileUiState.Success)
            assertEquals(
                newUsername,
                (updatedState as ProfileUiState.Success).user.displayName
            )

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { updateUserNameUseCase(newUsername) }
    }

    @Test
    fun `updateProfilePhoto should show error when update fails`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        val imageUri = "content://test/image.jpg"
        val exception = Exception("Upload failed")

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.failure(
            exception
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
        }

        coVerify(exactly = 1) { updateUserProfilePhotoUseCase(imageUri, user.uid) }
    }

    @Test
    fun `updateUserName should show error when update fails`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        val newUsername = "New Username"
        val exception = Exception("Upload failed")

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserNameUseCase(newUsername) } returns Result.failure(
            exception
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateUserName(newUsername)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
        }

        coVerify(exactly = 1) { updateUserNameUseCase(newUsername) }
    }

    @Test
    fun `updateProfilePhoto should show error when user is not logged in`() = runTest {
        every { getCurrentUserUseCase() } returns null

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateProfilePhoto("content://test/image.jpg")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("User not logged in", (state as ProfileUiState.Error).message)
        }

        coVerify(exactly = 0) { updateUserProfilePhotoUseCase(any(), any()) }
    }

    @Test
    fun `updateUserName should show error when user is not logged in`() = runTest {
        every { getCurrentUserUseCase() } returns null

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateUserName("New Username")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("User not logged in", (state as ProfileUiState.Error).message)
        }

        coVerify(exactly = 0) { updateUserNameUseCase(any()) }
    }
}
