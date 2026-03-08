package com.example.core_kt.utils

import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DateUtilsTest {

    @Test
    fun `formatDate should return correctly formatted date`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 27)
        }
        val millis = calendar.timeInMillis

        // When
        val result = formatDate(millis)

        // Then
        // Note: SimpleDateFormat uses Locale.getDefault(), which might vary.
        // But for "MM/dd/yyyy", it's usually consistent.
        assertEquals("10/27/2023", result)
    }

    @Test
    fun `formatTime should return correctly formatted time`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 30)
        }
        val millis = calendar.timeInMillis

        // When
        val result = formatTime(millis)

        // Then
        assertEquals("14:30", result)
    }
}
