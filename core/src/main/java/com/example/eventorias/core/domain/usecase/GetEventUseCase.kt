package com.example.eventorias.core.domain.usecase

import com.example.eventorias.core.domain.model.Evento
import com.example.network.data.repository.FirestoreRepository

class GetEventUseCase(private val repository: FirestoreRepository) {
    suspend operator fun invoke(eventId: String): Result<Evento?> {
        return repository.getEvent(eventId)
    }
}
