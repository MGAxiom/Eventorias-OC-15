package com.example.data.repository

import com.example.domain.repository.GoogleMapStaticRepository
import java.net.URLEncoder

class GoogleMapStaticRepositoryImpl(private val apiKey: String) : GoogleMapStaticRepository {

    override fun getStaticMapUrl(
        address: String,
        zoom: Int,
        width: Int,
        height: Int,
        mapType: String
    ): String {
        val encodedAddress = URLEncoder.encode(address, "UTF-8")
        return "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=$encodedAddress" +
                "&zoom=$zoom" +
                "&size=${width}x${height}" +
                "&maptype=$mapType" +
                "&key=$apiKey" +
                "&markers=color:red%7C$encodedAddress"
    }
}