package com.example.eventorias.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class DateTimePart { DATE, TIME }

fun updateEventDateTime(currentDate: Date?, newPartString: String, partToUpdate: DateTimePart): Date {
    val calendar = Calendar.getInstance()
    currentDate?.let { calendar.time = it }

    try {
        when (partToUpdate) {
            DateTimePart.DATE -> {
                val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(newPartString)
                parsedDate?.let {
                    val newDateCalendar = Calendar.getInstance().apply { time = it }
                    calendar.set(
                        newDateCalendar.get(Calendar.YEAR),
                        newDateCalendar.get(Calendar.MONTH),
                        newDateCalendar.get(Calendar.DAY_OF_MONTH)
                    )
                }
            }
            DateTimePart.TIME -> {
                val parsedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(newPartString)
                parsedTime?.let {
                    val newTimeCalendar = Calendar.getInstance().apply { time = it }
                    calendar.set(Calendar.HOUR_OF_DAY, newTimeCalendar.get(Calendar.HOUR_OF_DAY))
                    calendar.set(Calendar.MINUTE, newTimeCalendar.get(Calendar.MINUTE))
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                }
            }
        }
    } catch (e: Exception) {
        print(e)
    }
    return calendar.time
}