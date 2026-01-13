package com.example.eventorias.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventorias.core.domain.model.Evento
import com.example.eventorias.core.utils.DateTimePart
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.example.network.data.repository.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.eventorias.core.utils.updateEventDateTime

abstract class EventViewModel : ViewModel() {
    abstract val uiState: StateFlow<EventUiState>
    abstract fun getAllEvents()
    abstract fun getEventById(id: String)
    abstract fun addEvent(event: Evento)
}

internal class EventViewModelImpl(
    private val firestoreRepository: FirestoreRepository
) : EventViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    override val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private var _event = MutableStateFlow<Evento?>(null)
    val event: StateFlow<Evento?> = _event.asStateFlow()

    init {
        getAllEvents()
    }

    override fun getAllEvents() {
        viewModelScope.launch {
            firestoreRepository.getAllEvents()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessages = e.localizedMessage
                        )
                    }
                }
                .collect { events ->
                    _uiState.update { it.copy(isLoading = false, events = events) }
                }
        }
    }

    override fun getEventById(id: String) {

    }

    override fun addEvent(event: Evento) {
        TODO("Not yet implemented")
    }

    fun onAction(formEvent: FormEvent) {
        val updatedEvent = when (formEvent) {
            is FormEvent.TitleChanged ->
                _event.value?.copy(name = formEvent.title)

            is FormEvent.DescriptionChanged ->
                _event.value?.copy(description = formEvent.description)

            is FormEvent.DateChanged ->
                _event.value?.copy(date = updateEventDateTime(_event.value?.date, formEvent.date, DateTimePart.DATE))

            is FormEvent.TimeChanged ->
                _event.value?.copy(date = updateEventDateTime(_event.value?.date, formEvent.time, DateTimePart.TIME))

            is FormEvent.LocationChanged ->
                _event.value?.copy(location = formEvent.location)

            is FormEvent.PhotoUriChanged ->
                _event.value?.copy(photoUri = formEvent.photoUri)
        }
        _event.value = updatedEvent
    }
}
