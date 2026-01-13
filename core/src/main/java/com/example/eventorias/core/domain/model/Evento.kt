package com.example.eventorias.core.domain.model

import android.net.Uri
import java.util.Date

data class Evento(
    val id: String,
    val name: String,
    val date: Date,
    val description: String,
    val photoUri: Uri?,
    val location: String,
    val attachedUser: User
)
