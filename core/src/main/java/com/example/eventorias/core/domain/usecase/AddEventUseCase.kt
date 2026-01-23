package com.example.eventorias.core.domain.usecase

import com.example.eventorias.core.domain.model.Evento
import com.example.network.data.repository.FirestoreRepository

class AddEventUseCase(private val repository: FirestoreRepository) {
    suspend operator fun invoke(event: Evento): Result<Unit> {
        return repository.addEvent(event)
    }
}
