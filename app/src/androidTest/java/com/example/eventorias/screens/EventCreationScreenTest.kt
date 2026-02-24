package com.example.eventorias.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.eventorias.fake.FakeEventViewModel
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.screens.EventCreationScreen
import com.example.eventorias.ui.viewmodel.EventViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class EventCreationScreenTest {

    private val fakeEventViewModel = FakeEventViewModel()

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
        modules(module {
            single<EventViewModel> { fakeEventViewModel }
        })
    }

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun displaysAllFormFields() {
        composeTestRule.setContent {
            EventCreationScreen(onBack = {})
        }
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time").assertIsDisplayed()
        composeTestRule.onNodeWithText("Address").assertIsDisplayed()
    }

    @Test
    fun validate_withEmptyForm_showsValidationErrors() {
        composeTestRule.setContent {
            EventCreationScreen(onBack = {})
        }
        composeTestRule.onNodeWithText("Validate").performClick()
        composeTestRule.onNodeWithText("Title cannot be empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description cannot be empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Address cannot be empty").assertIsDisplayed()
    }

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            EventCreationScreen(onBack = {})
        }
        fakeEventViewModel.setUiState(EventUiState.Loading)
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun backButton_triggersCallback() {
        var backCalled = false
        composeTestRule.setContent {
            EventCreationScreen(onBack = { backCalled = true })
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backCalled)
    }
}
