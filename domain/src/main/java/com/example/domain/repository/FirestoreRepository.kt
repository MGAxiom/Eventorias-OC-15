package com.example.domain.repository

import com.example.domain.model.Evento
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun addEvent(event: Evento): Result<Unit>
    suspend fun getEvent(eventId: String): Result<Evento?>
    fun getAllEvents(): Flow<List<Evento>>
}