package com.example.eventorias.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.GetCurrentUserUseCase
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
}

internal class UserProfileViewModelImpl(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfilePhotoUseCase: UpdateUserProfilePhotoUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase
) : UserProfileViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = getCurrentUserUseCase()
        if (currentUser != null) {
            _uiState.value = ProfileUiState.Success(currentUser, currentUser.photoUrl)
        } else {
            _uiState.value = ProfileUiState.Error("User not logged in")
        }
    }

    override fun updateUserName(userName: String) {
        val currentUser = getCurrentUserUseCase()
        if (currentUser == null) {
            _uiState.value = ProfileUiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            updateUserNameUseCase(userName)
                .onSuccess {
                    val updatedUser = getCurrentUserUseCase()
                    _uiState.value = ProfileUiState.Success(updatedUser ?: currentUser, updatedUser?.photoUrl)
                }
                .onFailure { exception ->
                    _uiState.value = ProfileUiState.Error(
                        exception.message ?: "An error occured whilst trying to rename the user"
                    )
                    loadUserProfile()
                }
        }
    }

    override fun updateProfilePhoto(imageUri: String) {
        val currentUser = getCurrentUserUseCase()
        if (currentUser == null) {
            _uiState.value = ProfileUiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            updateUserProfilePhotoUseCase(imageUri, currentUser.uid)
                .onSuccess { photoUrl ->
                    val updatedUser = getCurrentUserUseCase()
                    _uiState.value = ProfileUiState.Success(updatedUser ?: currentUser, photoUrl)
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
