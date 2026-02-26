package com.example.domain.model

data class User(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val notificationsEnabled: Boolean = true
)