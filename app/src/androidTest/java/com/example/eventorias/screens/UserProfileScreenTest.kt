package com.example.eventorias.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.domain.model.User
import com.example.eventorias.fake.FakeUserProfileViewModel
import com.example.eventorias.ui.model.ProfileUiState
import com.example.eventorias.ui.screens.UserProfileScreen
import com.example.eventorias.ui.viewmodel.UserProfileViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class UserProfileScreenTest {

    private val fakeViewModel = FakeUserProfileViewModel(initialState = ProfileUiState.Loading)

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
        modules(module {
            single<UserProfileViewModel> { fakeViewModel }
        })
    }

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            UserProfileScreen()
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun successState_displaysUserName() {
        val user = User(uid = "1", displayName = "Jane Doe", email = "jane@example.com")
        fakeViewModel.setUiState(ProfileUiState.Success(user, null, false))
        composeTestRule.setContent {
            UserProfileScreen()
        }
        composeTestRule.onNodeWithText("Jane Doe").assertIsDisplayed()
    }

    @Test
    fun successState_displaysEmail() {
        val user = User(uid = "1", displayName = "Jane Doe", email = "jane@example.com")
        fakeViewModel.setUiState(ProfileUiState.Success(user, null, false))
        composeTestRule.setContent {
            UserProfileScreen()
        }
        composeTestRule.onNodeWithText("jane@example.com").assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        fakeViewModel.setUiState(ProfileUiState.Error("Something went wrong"))
        composeTestRule.setContent {
            UserProfileScreen()
        }
        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }
}
