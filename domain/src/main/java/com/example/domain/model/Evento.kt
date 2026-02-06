package com.example.domain.model

data class Evento(
    val id: String = "",
    val name: String = "",
    val date: Long = System.currentTimeMillis(),
    val description: String = "",
    val photoUri: String? = null,
    val photoUrl: String? = null,
    val location: String = "",
    val attachedUser: User? = null
)
