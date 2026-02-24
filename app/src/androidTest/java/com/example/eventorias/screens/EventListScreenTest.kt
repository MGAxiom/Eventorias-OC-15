package com.example.eventorias.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.model.Evento
import com.example.eventorias.ui.screens.EventListScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState_showsNoEventsText() {
        composeTestRule.setContent {
            EventListScreen(events = emptyList(), onEventClick = {})
        }
        composeTestRule.onNodeWithText("No events yet").assertIsDisplayed()
    }

    @Test
    fun emptyState_showsCreatePrompt() {
        composeTestRule.setContent {
            EventListScreen(events = emptyList(), onEventClick = {})
        }
        composeTestRule.onNodeWithText("Create your first event!").assertIsDisplayed()
    }

    @Test
    fun withEvents_displaysEventNames() {
        val events = listOf(
            Evento(id = "1", name = "Event One"),
            Evento(id = "2", name = "Event Two")
        )
        composeTestRule.setContent {
            EventListScreen(events = events, onEventClick = {})
        }
        composeTestRule.onNodeWithText("Event One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event Two").assertIsDisplayed()
    }

    @Test
    fun withEvents_clickingEvent_triggersCallback() {
        val events = listOf(Evento(id = "1", name = "Clickable Event"))
        var clickedId: String? = null
        composeTestRule.setContent {
            EventListScreen(events = events, onEventClick = { clickedId = it })
        }
        composeTestRule.onNodeWithText("Clickable Event").performClick()
        assertEquals("1", clickedId)
    }
}
