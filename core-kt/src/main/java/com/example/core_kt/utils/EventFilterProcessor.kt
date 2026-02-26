package com.example.core_kt.utils

import com.example.domain.model.Evento

fun filterEvents(
    events: List<Evento>,
    searchQuery: String,
    sortDescending: Boolean
): List<Evento> {
    val filteredEvents = events
        .filter { event ->
            if (searchQuery.isEmpty()) {
                true
            } else {
                event.name.contains(searchQuery, ignoreCase = true)
            }
        }
    return if (sortDescending) {
        filteredEvents.sortedByDescending { it.date }
    } else {
        filteredEvents.sortedBy { it.date }
    }
}
