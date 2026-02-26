package com.example.eventorias.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Evento
import com.example.domain.usecase.AddEventUseCase
import com.example.domain.usecase.GetAllEventsUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetEventUseCase
import com.example.domain.usecase.GetMapUrlUseCase
import com.example.domain.usecase.UploadImageUseCase
import com.example.eventorias.core.utils.DateTimePart
import com.example.eventorias.core.utils.updateEventDateTime
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Calendar

abstract class EventViewModel : ViewModel() {
    abstract val uiState: StateFlow<EventUiState>
    abstract val event: StateFlow<Evento>
    abstract val selectedEvent: StateFlow<Evento?> // ADD THIS
    abstract val eventSaved: StateFlow<Boolean>
    abstract val validationErrors: StateFlow<Map<String, String>>
    abstract fun getAllEvents()
    abstract fun getEventById(id: String)
    abstract fun addEvent()
    abstract fun onSaveComplete()
    abstract fun onAction(formEvent: FormEvent)
    abstract fun getMapUrl(address: String): String
}

internal class EventViewModelImpl(
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val getEventUseCase: GetEventUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMapUrlUseCase: GetMapUrlUseCase
) : EventViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Success(emptyList()))
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private var _event = MutableStateFlow(Evento())
    override val event: StateFlow<Evento> = _event.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Evento?>(null)
    override val selectedEvent: StateFlow<Evento?> = _selectedEvent.asStateFlow()

    private val _eventSaved = MutableStateFlow(false)
    override val eventSaved: StateFlow<Boolean> = _eventSaved.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    override val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    init {
        getAllEvents()
    }

    override fun getMapUrl(address: String): String {
        return getMapUrlUseCase(address)
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
        viewModelScope.launch {
            getEventUseCase(id)
                .onSuccess { event ->
                    _selectedEvent.value = event
                }
                .onFailure { e ->
                    _uiState.value = EventUiState.Error(e.message ?: "Failed to load event")
                }
        }
    }

    override fun addEvent() {
        // Validate all fields before submitting
        if (!validateAllFields()) {
            return
        }

        val eventToAdd = _event.value
        val currentUser = getCurrentUserUseCase()

        if (currentUser == null) {
            _uiState.value = EventUiState.Error("You must be logged in to create an event.")
            return
        }

        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val imageUrl = eventToAdd.photoUri?.let { uploadImageUseCase(it, currentUser.uid) }

                val finalEvent = eventToAdd.copy(
                    attachedUser = currentUser,
                    photoUrl = imageUrl,
                    photoUri = null
                )

                val result = addEventUseCase(finalEvent)

                result
                    .onSuccess {
                        getAllEvents()
                        _eventSaved.value = true
                        _validationErrors.value = emptyMap()
                    }
                    .onFailure { e ->
                        _uiState.value = EventUiState.Error(e.localizedMessage ?: "Failed to save event.")
                    }

            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(e.localizedMessage ?: "An error occurred during upload.")
            }
        }
    }

    private fun validateAllFields(): Boolean {
        val errors = mutableMapOf<String, String>()
        val eventToValidate = _event.value

        if (eventToValidate.name.isBlank()) {
            errors["title"] = "Title cannot be empty"
        }

        if (eventToValidate.description.isBlank()) {
            errors["description"] = "Description cannot be empty"
        }

        if (eventToValidate.location.isBlank()) {
            errors["address"] = "Address cannot be empty"
        }

        _validationErrors.value = errors
        return errors.isEmpty()
    }

    private fun validateField(fieldName: String, value: String) {
        val errors = _validationErrors.value.toMutableMap()

        when (fieldName) {
            "title" -> {
                if (value.isBlank()) {
                    errors["title"] = "Title cannot be empty"
                } else {
                    errors.remove("title")
                }
            }
            "description" -> {
                if (value.isBlank()) {
                    errors["description"] = "Description cannot be empty"
                } else {
                    errors.remove("description")
                }
            }
            "address" -> {
                if (value.isBlank()) {
                    errors["address"] = "Address cannot be empty"
                } else {
                    errors.remove("address")
                }
            }
        }

        _validationErrors.value = errors
    }

    override fun onAction(formEvent: FormEvent) {
        val updatedEvent = when (formEvent) {
            is FormEvent.TitleChanged -> {
                validateField("title", formEvent.title)
                _event.value.copy(name = formEvent.title)
            }

            is FormEvent.DescriptionChanged -> {
                validateField("description", formEvent.description)
                _event.value.copy(description = formEvent.description)
            }

            is FormEvent.DateChanged ->
                _event.value.copy(date = updateEventDateTime(_event.value.date, formEvent.date, DateTimePart.DATE))

            is FormEvent.TimeChanged ->
                _event.value.copy(date = updateEventDateTime(_event.value.date, formEvent.time, DateTimePart.TIME))

            is FormEvent.LocationChanged -> {
                validateField("address", formEvent.location)
                _event.value.copy(location = formEvent.location)
            }

            is FormEvent.PhotoUriChanged ->
                _event.value.copy(photoUri = formEvent.photoUri)
        }
        _event.value = updatedEvent
    }
}
