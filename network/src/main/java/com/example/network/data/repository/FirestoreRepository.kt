package com.example.network.data.repository

import com.example.eventorias.core.domain.model.Evento
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.toObject
//import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface FirestoreRepository {
    suspend fun addEvent(event: Evento): Result<Unit>
    suspend fun getEvent(eventId: String): Result<Evento?>
    fun getAllEvents(): Flow<List<Evento>>
}

class FirestoreRepositoryImpl(
    firestore: FirebaseFirestore
) : FirestoreRepository {

    private val eventsCollection = firestore.collection("events")

    override suspend fun addEvent(event: Evento): Result<Unit> = try {
        val document = if (event.id.isEmpty()) {
            eventsCollection.document()
        } else {
            eventsCollection.document(event.id)
        }
        val eventWithId = event.copy(id = document.id)
        document.set(eventWithId).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getEvent(eventId: String): Result<Evento?> = try {
        val documentSnapshot = eventsCollection.document(eventId).get().await()
        val event = documentSnapshot.toObject<Evento>()
        Result.success(event)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllEvents(): Flow<List<Evento>> {
        return eventsCollection.snapshots().map { snapshot ->
            snapshot.toObjects<Evento>()
        }
    }
}
