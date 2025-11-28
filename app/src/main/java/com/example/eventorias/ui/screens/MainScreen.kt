package com.example.eventorias.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.eventorias.model.Evento
import java.util.Date

@Composable
fun MainScreen(
    onNavigateToEventCreation: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.Events) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                NavigationBarItem(
                    selected = selectedTab == MainTab.Events,
                    onClick = { selectedTab = MainTab.Events },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Events") },
                    label = { Text("Events") }
                )
                NavigationBarItem(
                    selected = selectedTab == MainTab.Profile,
                    onClick = { selectedTab = MainTab.Profile },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            MainTab.Events -> {
                val sampleEvents = List(10) { index ->
                    Evento(
                        name = "Evento ${index + 1}",
                        date = Date(),
                        id = "$index",
                        attachedUser = com.example.eventorias.model.User(
                            name = "User ${index + 1}",
                            id = index.toLong(),
                            profilePicture = ""
                        )
                    )
                }
                EventListScreen(
                    modifier = modifier,
                    events = sampleEvents,
                    onAddEventClick = onNavigateToEventCreation,
                    onEventClick = onNavigateToEventDetails
                )
            }
            MainTab.Profile -> {
                Box(modifier = modifier) {
                    UserProfileScreen()
                }
            }
        }
    }
}

enum class MainTab {
    Events, Profile
}
