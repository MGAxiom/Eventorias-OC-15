package com.example.domain.model

import com.sun.jndi.toolkit.url.Uri
import java.util.Date

data class Evento(
    val id: String = "",
    val name: String = "",
    val date: Date = Date(),
    val description: String = "",
    val photoUri: String? = null,
    val photoUrl: String? = null,
    val location: String = "",
    val attachedUser: User? = null
)
