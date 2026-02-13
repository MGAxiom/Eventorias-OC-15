package com.example.eventorias.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class UpdateDateTest {

    @Test
    fun `updateEventDateTime should update date part correctly`() {
        // Given - Start with January 15, 2024 at 10:30
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        // New date: March 20, 2024
        val newDateString = "20/03/2024"

        // When
        val updatedMillis = updateEventDateTime(originalMillis, newDateString, DateTimePart.DATE)

        // Then - Date should be updated, time should remain the same
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = updatedMillis

        assertEquals(2024, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.MARCH, resultCalendar.get(Calendar.MONTH))
        assertEquals(20, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(10, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, resultCalendar.get(Calendar.MINUTE))
    }

    @Test
    fun `updateEventDateTime should update time part correctly`() {
        // Given - Start with January 15, 2024 at 10:30
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        // New time: 18:45
        val newTimeString = "18:45"

        // When
        val updatedMillis = updateEventDateTime(originalMillis, newTimeString, DateTimePart.TIME)

        // Then - Time should be updated, date should remain the same
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = updatedMillis

        assertEquals(2024, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, resultCalendar.get(Calendar.MONTH))
        assertEquals(15, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(45, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
        assertEquals(0, resultCalendar.get(Calendar.MILLISECOND))
    }

    @Test
    fun `updateEventDateTime should handle invalid date format gracefully`() {
        // Given
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        // Invalid date format
        val invalidDateString = "invalid-date"

        // When
        val updatedMillis = updateEventDateTime(originalMillis, invalidDateString, DateTimePart.DATE)

        // Then - Original date should remain unchanged
        assertEquals(originalMillis, updatedMillis)
    }

    @Test
    fun `updateEventDateTime should handle invalid time format gracefully`() {
        // Given
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        // Invalid time format
        val invalidTimeString = "invalid-time"

        // When
        val updatedMillis = updateEventDateTime(originalMillis, invalidTimeString, DateTimePart.TIME)

        // Then - Original time should remain unchanged
        assertEquals(originalMillis, updatedMillis)
    }

    @Test
    fun `updateEventDateTime should set seconds and milliseconds to zero when updating time`() {
        // Given
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 45)
        originalCalendar.set(Calendar.MILLISECOND, 500)
        val originalMillis = originalCalendar.timeInMillis

        // New time
        val newTimeString = "14:15"

        // When
        val updatedMillis = updateEventDateTime(originalMillis, newTimeString, DateTimePart.TIME)

        // Then
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = updatedMillis

        assertEquals(14, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(15, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
        assertEquals(0, resultCalendar.get(Calendar.MILLISECOND))
    }
}
