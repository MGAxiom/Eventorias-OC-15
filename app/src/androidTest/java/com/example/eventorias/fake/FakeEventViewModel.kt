package com.example.eventorias.fake

import com.example.domain.model.Evento
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.example.eventorias.ui.viewmodel.EventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeEventViewModel : EventViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Success(emptyList()))
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val _event = MutableStateFlow(Evento())
    override val event: StateFlow<Evento> = _event.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Evento?>(null)
    override val selectedEvent: StateFlow<Evento?> = _selectedEvent.asStateFlow()

    private val _eventSaved = MutableStateFlow(false)
    override val eventSaved: StateFlow<Boolean> = _eventSaved.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    override val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    fun setUiState(state: EventUiState) {
        _uiState.value = state
    }

    override fun getAllEvents() {}

    override fun getEventById(id: String) {}

    override fun addEvent() {
        val errors = mutableMapOf<String, String>()
        val current = _event.value
        if (current.name.isBlank()) errors["title"] = "Title cannot be empty"
        if (current.description.isBlank()) errors["description"] = "Description cannot be empty"
        if (current.location.isBlank()) errors["address"] = "Address cannot be empty"
        _validationErrors.value = errors
    }

    override fun onSaveComplete() {
        _eventSaved.value = false
        _event.value = Evento()
    }

    override fun onAction(formEvent: FormEvent) {
        val updated = when (formEvent) {
            is FormEvent.TitleChanged -> _event.value.copy(name = formEvent.title)
            is FormEvent.DescriptionChanged -> _event.value.copy(description = formEvent.description)
            is FormEvent.DateChanged -> _event.value
            is FormEvent.TimeChanged -> _event.value
            is FormEvent.LocationChanged -> _event.value.copy(location = formEvent.location)
            is FormEvent.PhotoUriChanged -> _event.value.copy(photoUri = formEvent.photoUri)
        }
        _event.value = updated
    }

    override fun getMapUrl(address: String): String = ""
}
