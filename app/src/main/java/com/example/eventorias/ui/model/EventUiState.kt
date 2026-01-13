package com.example.eventorias.ui.model

import com.example.eventorias.core.domain.model.Evento

data class EventUiState(
    val events: List<Evento> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: String? = null
)
