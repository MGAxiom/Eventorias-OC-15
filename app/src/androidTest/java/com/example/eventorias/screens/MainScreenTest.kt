package com.example.eventorias.screens

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit4.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.eventorias.fake.FakeEventViewModel
import com.example.eventorias.fake.FakeUserProfileViewModel
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.ProfileUiState
import com.example.eventorias.ui.screens.MainScreen
import com.example.eventorias.ui.viewmodel.EventViewModel
import com.example.eventorias.ui.viewmodel.UserProfileViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    private val fakeEventViewModel = FakeEventViewModel()
    private val fakeUserProfileViewModel = FakeUserProfileViewModel(
        initialState = ProfileUiState.Loading
    )

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
        modules(module {
            single<EventViewModel> { fakeEventViewModel }
            single<UserProfileViewModel> { fakeUserProfileViewModel }
        })
    }

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNav_showsEventsAndProfileTabs() {
        composeTestRule.setContent {
            MainScreen(onNavigateToEventCreation = {}, onNavigateToEventDetails = {})
        }
        composeTestRule.onNodeWithText("Events").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun eventsTab_fabIsVisible() {
        composeTestRule.setContent {
            MainScreen(onNavigateToEventCreation = {}, onNavigateToEventDetails = {})
        }
        composeTestRule.onNodeWithContentDescription("Add").assertIsDisplayed()
    }

    @Test
    fun eventsTab_loadingState_showsProgressIndicator() {
        fakeEventViewModel.setUiState(EventUiState.Loading)
        composeTestRule.setContent {
            MainScreen(onNavigateToEventCreation = {}, onNavigateToEventDetails = {})
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun eventsTab_errorState_showsErrorMessage() {
        fakeEventViewModel.setUiState(EventUiState.Error("Failed to load events"))
        composeTestRule.setContent {
            MainScreen(onNavigateToEventCreation = {}, onNavigateToEventDetails = {})
        }
        composeTestRule.onNodeWithText("Failed to load events").assertIsDisplayed()
    }

    @Test
    fun switchingToProfileTab_hides_fab() {
        composeTestRule.setContent {
            MainScreen(onNavigateToEventCreation = {}, onNavigateToEventDetails = {})
        }
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithContentDescription("Add").assertDoesNotExist()
    }
}
