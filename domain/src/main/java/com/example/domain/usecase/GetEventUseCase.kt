package com.example.domain.usecase

import com.example.domain.model.Evento
import com.example.domain.repository.FirestoreRepository

class GetEventUseCase(private val repository: FirestoreRepository) {
    suspend operator fun invoke(eventId: String): Result<Evento?> {
        return repository.getEvent(eventId)
    }
}
