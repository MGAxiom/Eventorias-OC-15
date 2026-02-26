package com.example.eventorias.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.AreNotificationsEnabledUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.SetNotificationsEnabledUseCase
import com.example.domain.usecase.UpdateUserNameUseCase
import com.example.domain.usecase.UpdateUserProfilePhotoUseCase
import com.example.eventorias.ui.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class UserProfileViewModel : ViewModel() {
    abstract val uiState: StateFlow<ProfileUiState>
    abstract fun updateProfilePhoto(imageUri: String)
    abstract fun updateUserName(userName: String)
    abstract fun setNotificationsEnabled(enabled: Boolean)
}

internal class UserProfileViewModelImpl(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase,
    private val areNotificationsEnabledUseCase: AreNotificationsEnabledUseCase,
    private val updateUserProfilePhotoUseCase: UpdateUserProfilePhotoUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase
) : UserProfileViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadUserNotificationPreferences()
    }

    private fun loadUserProfile() {
        val currentUser = fetchCurrentUser()
        currentUser?.let {
            _uiState.value = ProfileUiState.Success(
                user = currentUser,
                profilePhotoUrl = currentUser.photoUrl,
                notificationsEnabled = currentUser.notificationsEnabled
            )
        }
    }

    override fun updateUserName(userName: String) {
        viewModelScope.launch {
            val previousState = _uiState.value
            _uiState.value = ProfileUiState.Loading

            val currentUser = fetchCurrentUser()
            currentUser?.let {
                updateUserNameUseCase(userName)
                    .onSuccess { newUserName ->
                        if (previousState is ProfileUiState.Success) {
                            _uiState.value = previousState.copy(
                                user = previousState.user.copy(displayName = newUserName)
                            )
                        }
                    }
                    .onFailure { exception ->
                        _uiState.value = ProfileUiState.Error(
                            exception.message ?: "Failed to update user name"
                        )
                        loadUserProfile()
                    }
            }
        }
    }

    override fun updateProfilePhoto(imageUri: String) {
        viewModelScope.launch {
            val previousState = _uiState.value
            _uiState.value = ProfileUiState.Loading

            val currentUser = fetchCurrentUser()
            currentUser?.let {
                updateUserProfilePhotoUseCase(imageUri, currentUser.uid)
                    .onSuccess { newPhotoUrl ->
                        if (previousState is ProfileUiState.Success) {
                            _uiState.value = previousState.copy(profilePhotoUrl = newPhotoUrl)
                        }
                    }
                    .onFailure { exception ->
                        _uiState.value = ProfileUiState.Error(
                            exception.message ?: "Failed to update profile photo"
                        )
                        loadUserProfile()
                    }
            }
        }
    }

    private fun loadUserNotificationPreferences() {
        viewModelScope.launch {
            areNotificationsEnabledUseCase().collect { enabled ->
                val current = _uiState.value
                if (current is ProfileUiState.Success) {
                    _uiState.value = current.copy(notificationsEnabled = enabled)
                }
            }
        }
    }

    override fun setNotificationsEnabled(enabled: Boolean) {
        val currentUser = fetchCurrentUser()

        viewModelScope.launch {
            currentUser?.let {
                setNotificationsEnabledUseCase(enabled)
                    .onSuccess {
                        val current = _uiState.value
                        if (current is ProfileUiState.Success) {
                            _uiState.value = current.copy(notificationsEnabled = enabled)
                        }
                    }
                    .onFailure { exception ->
                        _uiState.value = ProfileUiState.Error(
                            exception.message ?: "Failed to update notification settings"
                        )
                    }
            }
        }
    }

    private fun fetchCurrentUser(): User? {
        return getCurrentUserUseCase() ?: run {
            _uiState.value = ProfileUiState.Error("User not logged in")
            null
        }
    }
}
