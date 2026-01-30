package com.example.domain.usecase

import com.example.domain.model.Evento
import com.example.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow

class GetAllEventsUseCase(private val repository: FirestoreRepository) {
    operator fun invoke(): Flow<List<Evento>> {
        return repository.getAllEvents()
    }
}
