package com.example.eventorias.fake

import com.example.eventorias.ui.model.ProfileUiState
import com.example.eventorias.ui.viewmodel.UserProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeUserProfileViewModel(
    initialState: ProfileUiState = ProfileUiState.Loading
) : UserProfileViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun setUiState(state: ProfileUiState) {
        _uiState.value = state
    }

    override fun updateProfilePhoto(imageUri: String) {}

    override fun updateUserName(userName: String) {}

    override fun setNotificationsEnabled(enabled: Boolean) {}
}
