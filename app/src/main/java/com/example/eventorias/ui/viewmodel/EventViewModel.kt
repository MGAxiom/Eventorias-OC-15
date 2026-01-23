package com.example.eventorias.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventorias.core.domain.model.Evento
import com.example.eventorias.core.domain.model.User
import com.example.eventorias.core.domain.usecase.AddEventUseCase
import com.example.eventorias.core.domain.usecase.GetAllEventsUseCase
import com.example.eventorias.core.utils.DateTimePart
import com.example.eventorias.core.utils.updateEventDateTime
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class EventViewModel : ViewModel() {
    abstract val uiState: StateFlow<EventUiState>
    abstract val event: StateFlow<Evento>
    abstract val eventSaved: StateFlow<Boolean>
    abstract fun getAllEvents()
    abstract fun getEventById(id: String)
    abstract fun addEvent()
    abstract fun onSaveComplete()
    abstract fun onAction(formEvent: FormEvent)
}

internal class EventViewModelImpl(
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : EventViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Success(emptyList()))
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private var _event = MutableStateFlow(Evento())
    override val event: StateFlow<Evento> = _event.asStateFlow()

    private val _eventSaved = MutableStateFlow(false)
    override val eventSaved: StateFlow<Boolean> = _eventSaved.asStateFlow()
    private val storageRef: StorageReference = storage.reference


    init {
        getAllEvents()
    }

    override fun onSaveComplete() {
        _eventSaved.value = false
        _event.value = Evento()
    }

    override fun getAllEvents() {
        viewModelScope.launch {
            getAllEventsUseCase()
                .onStart { _uiState.value = EventUiState.Loading }
                .catch { e -> _uiState.value = EventUiState.Error(e.message ?: "Unknown error") }
                .collect { events -> _uiState.value = EventUiState.Success(events) }
        }
    }

    override fun getEventById(id: String) {
        // TODO: Implement logic to fetch a single event from the repository
    }

    override fun addEvent() {
        val eventToAdd = _event.value
        val currentUser = auth.currentUser

        if (eventToAdd.name.isBlank()) {
            _uiState.value = EventUiState.Error("Event title cannot be empty.")
            return
        }
        if (currentUser == null) {
            _uiState.value = EventUiState.Error("You must be logged in to create an event.")
            return
        }

        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val imageUrl = eventToAdd.photoUri?.let { uploadImage(it.toString().toUri(), currentUser.uid) }

                val finalEvent = eventToAdd.copy(
                    attachedUser = User(
                        id = currentUser.uid,
                        name = currentUser.displayName ?: "Unknown User",
                        profilePicture = currentUser.photoUrl.toString()
                    ),
                    photoUrl = imageUrl,
                    photoUri = null // Clear local URI before saving to Firestore
                )

                addEventUseCase(finalEvent)
                    .onSuccess {
                        _uiState.value = EventUiState.Success(emptyList()) // Reset to success state
                        _eventSaved.value = true
                    }
                    .onFailure { e ->
                        _uiState.value = EventUiState.Error(e.localizedMessage ?: "Failed to save event.")
                    }
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(e.localizedMessage ?: "An error occurred during upload.")
            }
        }
    }

    private suspend fun uploadImage(imageUri: Uri, userId: String): String {
        val fileName = "event_images/${userId}/${UUID.randomUUID()}"
        val imageRef = storage.reference.child(fileName)
        imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }

    override fun onAction(formEvent: FormEvent) {
        val updatedEvent = when (formEvent) {
            is FormEvent.TitleChanged ->
                _event.value.copy(name = formEvent.title)

            is FormEvent.DescriptionChanged ->
                _event.value.copy(description = formEvent.description)

            is FormEvent.DateChanged ->
                _event.value.copy(date = updateEventDateTime(_event.value.date, formEvent.date, DateTimePart.DATE))

            is FormEvent.TimeChanged ->
                _event.value.copy(date = updateEventDateTime(_event.value.date, formEvent.time, DateTimePart.TIME))

            is FormEvent.LocationChanged ->
                _event.value.copy(location = formEvent.location)

            is FormEvent.PhotoUriChanged ->
                _event.value.copy(photoUri = formEvent.photoUri)
        }
        _event.value = updatedEvent
    }
}
