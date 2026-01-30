package com.example.domain.repository

interface GoogleMapStaticRepository {
    fun getStaticMapUrl(address: String, zoom: Int = 15, width: Int = 400, height: Int = 400, mapType: String = "roadmap"): String
}