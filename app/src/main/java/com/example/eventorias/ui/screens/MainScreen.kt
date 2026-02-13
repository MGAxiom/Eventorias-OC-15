package com.example.eventorias.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core_ui.components.EventTopBar
import com.example.eventorias.R
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.viewmodel.EventViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    onNavigateToEventCreation: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit,
    viewModel: EventViewModel = koinInject()
) {
    var selectedTab by remember { mutableStateOf(MainTab.Events) }
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var sortDescending by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            if (selectedTab == MainTab.Events) {
                EventTopBar(
                    filterIcon = R.drawable.eventorias_filter_icon,
                    searchQuery = searchQuery,
                    isSearchVisible = isSearchVisible,
                    onSearchQueryChange = { searchQuery = it },
                    onSearchToggle = {
                        isSearchVisible = !isSearchVisible
                        if (!isSearchVisible) {
                            searchQuery = ""
                        }
                    },
                    onFilterToggle = { sortDescending = !sortDescending },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                )
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
                when (uiState) {
                    is EventUiState.Loading -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is EventUiState.Success -> {
                        val allEvents = (uiState as EventUiState.Success).events

                        val filteredEvents = allEvents
                            .filter { event ->
                                if (searchQuery.isEmpty()) {
                                    true
                                } else {
                                    event.name.contains(searchQuery, ignoreCase = true)
                                }
                            }
                            .sortedBy { event -> event.date }
                            .let { if (sortDescending) it.reversed() else it }

                        EventListScreen(
                            modifier = modifier,
                            events = filteredEvents,
                            onEventClick = onNavigateToEventDetails
                        )
                    }

                    is EventUiState.Error -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (uiState as EventUiState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
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
