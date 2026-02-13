package com.example.data.repository

import android.util.Log
import com.example.data.mapper.toDomain
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun getCurrentUser(): User? = firebaseAuth.currentUser?.toDomain()

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun deleteCurrentUserAccount(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = firebaseAuth.currentUser
        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.let { onFailure(it) }
                    Log.v("AuthRepository", "deleteCurrentUserAccount: ${task.exception}")
                }
            } ?: onFailure(IllegalStateException("User not found to delete."))
    }

    override suspend fun updateUserProfile(displayName: String?, photoUrl: String?): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(IllegalStateException("No user logged in"))

            val profileUpdates = userProfileChangeRequest {
                displayName?.let { this.displayName = it }
                photoUrl?.let { this.photoUri = android.net.Uri.parse(it) }
            }

            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "updateUserProfile failed", e)
            Result.failure(e)
        }
    }
}