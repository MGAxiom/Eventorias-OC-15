package com.example.eventorias.model

import java.util.Date

data class Evento(
    val id: String,
    val name: String,
    val date: Date,
    val attachedUser: User
)
