package com.example.eventorias.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable data object Login : Screen
    @Serializable data object Main : Screen
    @Serializable data object EventCreation : Screen
    @Serializable data class EventDetails(val eventId: String) : Screen
}