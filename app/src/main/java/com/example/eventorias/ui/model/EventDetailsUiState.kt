package com.example.eventorias.ui.model

data class EventDetailsUiState(
    val title: String,
    val description: String,
    val address: String,
    val date: String,
    val time: String,
    val imageUrl: Int,
    val mapImageUrl: String,
    val authorImageUrl: Int
)
