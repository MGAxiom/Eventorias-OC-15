package com.example.eventorias

import android.app.Application

/**
 * Test Application that does not start Koin, allowing KoinTestRule to manage
 * the Koin lifecycle in each test class.
 */
class TestEventoriasApp : Application()
