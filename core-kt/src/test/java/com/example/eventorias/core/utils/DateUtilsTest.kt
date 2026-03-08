package com.example.eventorias.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    @Test
    fun `formatDate should format milliseconds to MM-dd-yyyy format`() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 10, 30, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        val formattedDate = formatDate(millis)

        assertEquals("01/15/2024", formattedDate)
    }

    @Test
    fun `formatTime should format milliseconds to HH-mm format`() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 14, 45, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        val formattedTime = formatTime(millis)

        assertEquals("14:45", formattedTime)
    }

    @Test
    fun `formatTime should format midnight correctly`() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val millis = calendar.timeInMillis

        val formattedTime = formatTime(millis)

        assertEquals("00:00", formattedTime)
    }
}
