package com.example.core_kt.utils

import com.example.domain.model.Evento
import com.example.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EventFilterProcessorTest {

    private val emptyUser = User("", "", "", "")

    @Test
    fun `filterEvents should return empty list when input is empty`() {
        val result = filterEvents(emptyList(), "search", false)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filterEvents should return all events when search query is empty`() {
        val events = listOf(
            Evento(id = "1", name = "A", date = 100),
            Evento(id = "2", name = "B", date = 200)
        )
        val result = filterEvents(events, "", false)
        assertEquals(2, result.size)
    }

    @Test
    fun `filterEvents should filter by name case-insensitively`() {
        val events = listOf(
            Evento(id = "1", name = "Android Event"),
            Evento(id = "2", name = "Kotlin Party"),
            Evento(id = "3", name = "android workshop")
        )
        val result = filterEvents(events, "android", false)
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Android Event" })
        assertTrue(result.any { it.name == "android workshop" })
    }

    @Test
    fun `filterEvents should return empty list when no matches found`() {
        val events = listOf(
            Evento(id = "1", name = "A"),
            Evento(id = "2", name = "B")
        )
        val result = filterEvents(events, "Z", false)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filterEvents should sort by date ascending`() {
        val events = listOf(
            Evento(id = "1", name = "Late", date = 300),
            Evento(id = "2", name = "Early", date = 100),
            Evento(id = "3", name = "Middle", date = 200)
        )
        val result = filterEvents(events, "", false)
        assertEquals("Early", result[0].name)
        assertEquals("Middle", result[1].name)
        assertEquals("Late", result[2].name)
    }

    @Test
    fun `filterEvents should sort by date descending`() {
        val events = listOf(
            Evento(id = "1", name = "Late", date = 300),
            Evento(id = "2", name = "Early", date = 100),
            Evento(id = "3", name = "Middle", date = 200)
        )
        val result = filterEvents(events, "", true)
        assertEquals("Late", result[0].name)
        assertEquals("Middle", result[1].name)
        assertEquals("Early", result[2].name)
    }

    @Test
    fun `filterAndSort should work together`() {
        val events = listOf(
            Evento(id = "1", name = "Android 2", date = 200),
            Evento(id = "2", name = "iOS", date = 100),
            Evento(id = "3", name = "Android 1", date = 50)
        )
        val result = filterEvents(events, "Android", true)
        assertEquals(2, result.size)
        assertEquals("Android 2", result[0].name)
        assertEquals("Android 1", result[1].name)
    }
}
