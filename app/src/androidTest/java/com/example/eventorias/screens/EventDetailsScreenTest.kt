package com.example.eventorias.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eventorias.ui.model.EventDetailsUiState
import com.example.eventorias.ui.screens.EventDetailsScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleState = EventDetailsUiState(
        title = "Art Exhibition",
        description = "A wonderful art exhibition.",
        address = "123 Main St, Paris",
        date = "July 20, 2024",
        time = "10:00 AM",
        imageUrl = null,
        mapImageUrl = "",
        authorImageUrl = null
    )

    @Test
    fun displaysTitle() {
        composeTestRule.setContent {
            EventDetailsScreen(uiState = sampleState)
        }
        composeTestRule.onNodeWithText("Art Exhibition").assertIsDisplayed()
    }

    @Test
    fun displaysDescription() {
        composeTestRule.setContent {
            EventDetailsScreen(uiState = sampleState)
        }
        composeTestRule.onNodeWithText("A wonderful art exhibition.").assertIsDisplayed()
    }

    @Test
    fun displaysDateAndTime() {
        composeTestRule.setContent {
            EventDetailsScreen(uiState = sampleState)
        }
        composeTestRule.onNodeWithText("July 20, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("10:00 AM").assertIsDisplayed()
    }

    @Test
    fun displaysAddress() {
        composeTestRule.setContent {
            EventDetailsScreen(uiState = sampleState)
        }
        composeTestRule.onNodeWithText("123 Main St, Paris").assertIsDisplayed()
    }

    @Test
    fun backButton_triggersCallback() {
        var backCalled = false
        composeTestRule.setContent {
            EventDetailsScreen(uiState = sampleState, onBack = { backCalled = true })
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backCalled)
    }
}
