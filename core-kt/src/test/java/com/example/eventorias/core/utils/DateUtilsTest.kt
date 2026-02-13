package com.example.eventorias.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    @Test
    fun `formatDate should format milliseconds to MM-dd-yyyy format`() {
        // Given - January 15, 2024 at 10:30 AM
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        // When
        val formattedDate = formatDate(millis)

        // Then
        assertEquals("01/15/2024", formattedDate)
    }

    @Test
    fun `formatTime should format milliseconds to HH-mm format`() {
        // Given - 14:45 (2:45 PM)
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 14, 45, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        // When
        val formattedTime = formatTime(millis)

        // Then
        assertEquals("14:45", formattedTime)
    }

    @Test
    fun `formatTime should format midnight correctly`() {
        // Given - 00:00 (midnight)
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        // When
        val formattedTime = formatTime(millis)

        // Then
        assertEquals("00:00", formattedTime)
    }
}
