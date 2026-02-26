package com.example.eventorias.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.domain.model.Evento
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import java.util.Date

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

    @Test
    fun whenSearchingForEvent_onlyMatchingEventsShouldBeVisible() {
        val events = listOf(
            Evento(id = "1", name = "Cool Event", date = Date().time, description = "", photoUrl = "", location = ""),
            Evento(id = "2", name = "Another Event", date = Date().time, description = "", photoUrl = "", location = "")
        )
        fakeEventViewModel.setUiState(EventUiState.Success(events))

        composeTestRule.setContent {
            MainScreen(
                onNavigateToEventCreation = {},
                onNavigateToEventDetails = {}
            )
        }

        composeTestRule.onNodeWithText("Cool Event").assertExists()
        composeTestRule.onNodeWithText("Another Event").assertExists()

        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithText("Search events by name...").performTextInput("Cool")

        composeTestRule.onNodeWithText("Cool Event").assertExists()
        composeTestRule.onNodeWithText("Another Event").assertDoesNotExist()
    }
}
