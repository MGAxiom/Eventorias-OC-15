package com.example.data.repository

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthRepositoryImplTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setup() {
        firebaseAuth = mockk(relaxed = true)
        authRepository = AuthRepositoryImpl(firebaseAuth)
    }

    @Test
    fun `getCurrentUser should return user when authenticated`() {
        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        every { firebaseUser.uid } returns "user123"
        every { firebaseUser.displayName } returns "Test User"
        every { firebaseUser.email } returns "test@example.com"
        every { firebaseUser.photoUrl } returns Uri.parse("https://example.com/photo.jpg")
        every { firebaseAuth.currentUser } returns firebaseUser

        val user = authRepository.getCurrentUser()

        assertNotNull(user)
        assertEquals("user123", user?.uid)
        assertEquals("Test User", user?.displayName)
        assertEquals("test@example.com", user?.email)
        assertEquals("https://example.com/photo.jpg", user?.photoUrl)
    }

    @Test
    fun `getCurrentUser should return null when not authenticated`() {
        every { firebaseAuth.currentUser } returns null

        val user = authRepository.getCurrentUser()

        assertNull(user)
    }

    @Test
    fun `signOut should call firebase signOut`() {
        authRepository.signOut()

        verify(exactly = 1) { firebaseAuth.signOut() }
    }

    @Test
    fun `deleteCurrentUserAccount should call delete on success`() {
        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        val task = mockk<Task<Void>>(relaxed = true)
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)

        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.delete() } returns task
        every { task.isSuccessful } returns true
        every { task.addOnCompleteListener(any()) } answers {
            val listener = firstArg<OnCompleteListener<Void>>()
            listener.onComplete(task)
            task
        }

        authRepository.deleteCurrentUserAccount(onSuccess, onFailure)

        verify(exactly = 1) { firebaseUser.delete() }
        verify(exactly = 1) { onSuccess() }
        verify(exactly = 0) { onFailure(any()) }
    }

    @Test
    fun `deleteCurrentUserAccount should call onFailure when user is null`() {
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)

        every { firebaseAuth.currentUser } returns null

        authRepository.deleteCurrentUserAccount(onSuccess, onFailure)

        verify(exactly = 0) { onSuccess() }
        verify { onFailure(any()) }
    }

    @Test
    fun `updateUserProfile should return success when update succeeds`() = runTest {
        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        val task = mockk<Task<Void>>(relaxed = true)
        val photoUrl = "https://example.com/photo.jpg"

        every { firebaseAuth.currentUser } returns firebaseUser
        every { task.isSuccessful } returns true
        every { task.isComplete } returns true
        every { task.exception } returns null
        every { task.isCanceled } returns false
        every { firebaseUser.updateProfile(any()) } returns task

        coEvery { task.result } returns mockk()

        val result = authRepository.updateUserProfile(photoUrl = photoUrl)

        assertTrue(result.isSuccess)
        verify { firebaseUser.updateProfile(any()) }
    }

    @Test
    fun `updateUserProfile should return failure when no user is logged in`() = runTest {
        every { firebaseAuth.currentUser } returns null

        val result = authRepository.updateUserProfile(photoUrl = "https://example.com/photo.jpg")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }
}
