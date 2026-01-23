package com.example.eventorias.ui.model

import com.example.eventorias.core.domain.model.Evento

sealed interface EventUiState {
    data class Success(val events: List<Evento>) : EventUiState
    data object Loading : EventUiState
    data class Error(val message: String) : EventUiState
}
