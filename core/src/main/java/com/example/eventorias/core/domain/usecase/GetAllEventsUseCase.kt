package com.example.eventorias.core.domain.usecase

import com.example.eventorias.core.domain.model.Evento
import com.example.network.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow

class GetAllEventsUseCase(private val repository: FirestoreRepository) {
    operator fun invoke(): Flow<List<Evento>> {
        return repository.getAllEvents()
    }
}
