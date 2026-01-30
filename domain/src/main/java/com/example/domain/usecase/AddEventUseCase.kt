package com.example.domain.usecase

import com.example.domain.model.Evento
import com.example.domain.repository.FirestoreRepository

class AddEventUseCase(private val repository: FirestoreRepository) {
    suspend operator fun invoke(event: Evento): Result<Unit> {
        return repository.addEvent(event)
    }
}
