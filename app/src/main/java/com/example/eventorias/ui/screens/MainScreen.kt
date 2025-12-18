package com.example.eventorias.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.example.eventorias.model.Evento
import com.example.eventorias.core.components.EventTopBar
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data object Home: NavKey

@Composable
fun MainScreen(
    onNavigateToEventCreation: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.Events) }

    Scaffold(
        topBar = {
            if (selectedTab == MainTab.Events) {
                EventTopBar(Modifier.padding(bottom = TOP_BAR_BOTTOM_PADDING.dp))
            }
        },
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
        },
        floatingActionButton = {
            if (selectedTab == MainTab.Events) {
                FloatingActionButton(
                    onClick = onNavigateToEventCreation,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
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
