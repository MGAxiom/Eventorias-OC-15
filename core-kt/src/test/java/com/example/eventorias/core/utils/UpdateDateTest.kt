package com.example.eventorias.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class UpdateDateTest {

    @Test
    fun `updateEventDateTime should update date part correctly`() {
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        val newDateString = "20/03/2024"

        val updatedMillis = updateEventDateTime(originalMillis, newDateString, DateTimePart.DATE)

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
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        val newTimeString = "18:45"

        val updatedMillis = updateEventDateTime(originalMillis, newTimeString, DateTimePart.TIME)

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
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        val invalidDateString = "invalid-date"

        val updatedMillis = updateEventDateTime(originalMillis, invalidDateString, DateTimePart.DATE)

        assertEquals(originalMillis, updatedMillis)
    }

    @Test
    fun `updateEventDateTime should handle invalid time format gracefully`() {
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        originalCalendar.set(Calendar.MILLISECOND, 0)
        val originalMillis = originalCalendar.timeInMillis

        val invalidTimeString = "invalid-time"

        val updatedMillis = updateEventDateTime(originalMillis, invalidTimeString, DateTimePart.TIME)

        assertEquals(originalMillis, updatedMillis)
    }

    @Test
    fun `updateEventDateTime should set seconds and milliseconds to zero when updating time`() {
        val originalCalendar = Calendar.getInstance()
        originalCalendar.set(2024, Calendar.JANUARY, 15, 10, 30, 45)
        originalCalendar.set(Calendar.MILLISECOND, 500)
        val originalMillis = originalCalendar.timeInMillis

        val newTimeString = "14:15"

        val updatedMillis = updateEventDateTime(originalMillis, newTimeString, DateTimePart.TIME)

        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = updatedMillis

        assertEquals(14, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(15, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
        assertEquals(0, resultCalendar.get(Calendar.MILLISECOND))
    }
}
