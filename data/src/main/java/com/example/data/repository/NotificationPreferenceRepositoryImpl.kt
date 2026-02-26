package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.repository.NotificationPreferenceRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import androidx.datastore.preferences.core.Preferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class NotificationPreferencesRepositoryImpl(
    private val context: Context,
    private val firebaseMessaging: FirebaseMessaging
) : NotificationPreferenceRepository {

    companion object {
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        private const val TOPIC_ALL_USERS = "all_users"
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean): Result<Unit> {
        return try {
            context.dataStore.edit { preferences ->
                preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
            }

            if (enabled) {
                firebaseMessaging.subscribeToTopic(TOPIC_ALL_USERS).await()
            } else {
                firebaseMessaging.unsubscribeFromTopic(TOPIC_ALL_USERS).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun areNotificationsEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
        }
    }
}