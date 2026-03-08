package com.example.data.repository

import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.URLEncoder

class GoogleMapStaticRepositoryImplTest {

    private val apiKey = "test_api_key"
    private val repository = GoogleMapStaticRepositoryImpl(apiKey)

    @Test
    fun `getStaticMapUrl should return valid url with parameters`() {
        val address = "1600 Amphitheatre Parkway, Mountain View, CA"
        val encodedAddress = URLEncoder.encode(address, "UTF-8")

        val url = repository.getStaticMapUrl(address)

        assertTrue(url.contains("center=$encodedAddress"))
        assertTrue(url.contains("zoom=15"))
        assertTrue(url.contains("size=400x400"))
        assertTrue(url.contains("maptype=roadmap"))
        assertTrue(url.contains("key=$apiKey"))
        assertTrue(url.contains("markers=color:red%7C$encodedAddress"))
    }
}
