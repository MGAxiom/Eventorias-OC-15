package com.example.domain.usecase

import com.example.domain.repository.GoogleMapStaticRepository

class GetMapUrlUseCase(private val repository: GoogleMapStaticRepository) {
    operator fun invoke(address: String): String = repository.getStaticMapUrl(address)
}