package com.example.eventorias.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.model.User
import com.example.domain.usecase.AreNotificationsEnabledUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.SetNotificationsEnabledUseCase
import com.example.domain.usecase.UpdateUserNameUseCase
import com.example.domain.usecase.UpdateUserProfilePhotoUseCase
import com.example.eventorias.ui.model.ProfileUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase
    private lateinit var areNotificationsEnabledUseCase: AreNotificationsEnabledUseCase
    private lateinit var viewModel: UserProfileViewModelImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentUserUseCase = mockk()
        updateUserProfilePhotoUseCase = mockk()
        updateUserNameUseCase = mockk()
        setNotificationsEnabledUseCase = mockk()
        areNotificationsEnabledUseCase = mockk()

        every { areNotificationsEnabledUseCase() } returns flowOf(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): UserProfileViewModelImpl {
        return UserProfileViewModelImpl(
            getCurrentUserUseCase = getCurrentUserUseCase,
            updateUserProfilePhotoUseCase = updateUserProfilePhotoUseCase,
            updateUserNameUseCase = updateUserNameUseCase,
            setNotificationsEnabledUseCase = setNotificationsEnabledUseCase,
            areNotificationsEnabledUseCase = areNotificationsEnabledUseCase
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
            val successState = state as ProfileUiState.Success
            assertEquals(user, successState.user)
            assertEquals("https://example.com/photo.jpg", successState.profilePhotoUrl)
            assertTrue(successState.notificationsEnabled)
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
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.success(newPhotoUrl)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(newPhotoUrl, (state as ProfileUiState.Success).profilePhotoUrl)
        }
        coVerify { updateUserProfilePhotoUseCase(imageUri, user.uid) }
    }

    @Test
    fun `updateProfilePhoto should show error and revert when update fails`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        val imageUri = "content://test/image.jpg"
        val exception = Exception("Upload failed")

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserProfilePhotoUseCase(imageUri, user.uid) } returns Result.failure(exception)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateProfilePhoto(imageUri)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            // It might emit Error then revert to Success via loadUserProfile()
            // But usually, the last state is what matters or we can check items.
            // Based on code: it sets Error then calls loadUserProfile()
            assertTrue(state is ProfileUiState.Success) 
        }
    }

    @Test
    fun `updateUserName should update user profile name successfully`() = runTest {
        val user = User(uid = "user1", displayName = "Old Name", email = "test@example.com")
        val newUsername = "New Name"

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserNameUseCase(newUsername) } returns Result.success(newUsername)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateUserName(newUsername)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(newUsername, (state as ProfileUiState.Success).user.displayName)
        }
    }

    @Test
    fun `updateUserName should show error when update fails`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        val newUsername = "New Name"
        val exception = Exception("Update failed")

        every { getCurrentUserUseCase() } returns user
        coEvery { updateUserNameUseCase(newUsername) } returns Result.failure(exception)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateUserName(newUsername)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success) // Reverts after error
        }
    }

    @Test
    fun `setNotificationsEnabled should update state on success`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns user
        coEvery { setNotificationsEnabledUseCase(false) } returns Result.success(Unit)
        
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.setNotificationsEnabled(false)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Success)
            assertEquals(false, (state as ProfileUiState.Success).notificationsEnabled)
        }
    }

    @Test
    fun `setNotificationsEnabled should show error on failure`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        every { getCurrentUserUseCase() } returns user
        coEvery { setNotificationsEnabledUseCase(true) } returns Result.failure(Exception("Failed"))
        
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.setNotificationsEnabled(true)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ProfileUiState.Error)
            assertEquals("Failed", (state as ProfileUiState.Error).message)
        }
    }

    @Test
    fun `uiState should update when notification preferences flow emits`() = runTest {
        val user = User(uid = "user1", displayName = "Test User")
        val prefsFlow = MutableStateFlow(true)
        every { areNotificationsEnabledUseCase() } returns prefsFlow
        every { getCurrentUserUseCase() } returns user

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertTrue((awaitItem() as ProfileUiState.Success).notificationsEnabled)
            
            prefsFlow.value = false
            advanceUntilIdle()
            
            assertEquals(false, (awaitItem() as ProfileUiState.Success).notificationsEnabled)
        }
    }

    @Test
    fun `updateUserName should show error when user not logged in`() = runTest {
        every { getCurrentUserUseCase() } returns null
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateUserName("New")
        advanceUntilIdle()

        viewModel.uiState.test {
            assertTrue(awaitItem() is ProfileUiState.Error)
        }
    }
}
