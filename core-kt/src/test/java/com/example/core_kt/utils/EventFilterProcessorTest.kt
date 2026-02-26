package com.example.core_kt.utils

import com.example.domain.model.Evento
import com.example.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

class EventFilterProcessorTest {

    @Test
    fun `filtering and sorting logic is correct`() {
        val emptyUser =  User("", "", "", "")
        val events = listOf(
            Evento(id = "1", name = "Event B", date = 100, attachedUser = emptyUser, description = "", photoUrl = "", location = ""),
            Evento(id = "2", name = "Event A", date = 200, attachedUser = emptyUser, description = "", photoUrl = "", location = ""),
            Evento(id = "3", name = "Another Event", date = 50, attachedUser = emptyUser, description = "", photoUrl = "", location = "")
        )

        val searchResult = filterEvents(events, "Event", false)
        assertEquals(3, searchResult.size)

        val sortedAsc = filterEvents(events, "", false)
        assertEquals("Another Event", sortedAsc[0].name)

        val sortedDesc = filterEvents(events, "", true)
        assertEquals("Event A", sortedDesc[0].name)
    }
}
