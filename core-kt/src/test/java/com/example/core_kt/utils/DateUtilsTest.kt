package com.example.core_kt.utils

import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    @Test
    fun `formatDate should return correctly formatted date`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 27)
        }
        val millis = calendar.timeInMillis

        val result = formatDate(millis)

        assertEquals("10/27/2023", result)
    }

    @Test
    fun `formatTime should return correctly formatted time`() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 30)
        }
        val millis = calendar.timeInMillis

        val result = formatTime(millis)

        assertEquals("14:30", result)
    }
}
