package com.example.core_kt.utils

import com.example.eventorias.core.utils.DateTimePart
import com.example.eventorias.core.utils.updateEventDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class UpdateDateTest {

    @Test
    fun `updateEventDateTime should update date part`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 1, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val initialMillis = calendar.timeInMillis
        val newDate = "25/12/2023"

        // When
        val result = updateEventDateTime(initialMillis, newDate, DateTimePart.DATE)

        // Then
        val resultCalendar = Calendar.getInstance().apply { timeInMillis = result }
        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, resultCalendar.get(Calendar.MONTH))
        assertEquals(25, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(12, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
    }

    @Test
    fun `updateEventDateTime should update time part`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 1, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val initialMillis = calendar.timeInMillis
        val newTime = "18:30"

        // When
        val result = updateEventDateTime(initialMillis, newTime, DateTimePart.TIME)

        // Then
        val resultCalendar = Calendar.getInstance().apply { timeInMillis = result }
        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, resultCalendar.get(Calendar.MONTH))
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, resultCalendar.get(Calendar.MINUTE))
    }

    @Test
    fun `updateEventDateTime should handle invalid date format gracefully`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 1, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val initialMillis = calendar.timeInMillis
        val invalidDate = "invalid-date"

        // When
        val result = updateEventDateTime(initialMillis, invalidDate, DateTimePart.DATE)

        // Then
        assertEquals(initialMillis, result)
    }
}
