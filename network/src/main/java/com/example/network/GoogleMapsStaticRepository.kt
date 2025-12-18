package com.example.network

import java.net.URLEncoder

class GoogleMapsStaticRepository(private val apiKey: String) {

    fun getStaticMapUrl(
        address: String,
        zoom: Int = 15,
        width: Int = 600,
        height: Int = 300,
        mapType: String = "roadmap"
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
