package com.example.eventorias.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventorias.model.Evento
import com.example.eventorias.ui.components.EventListItem

@Composable
fun EventListScreen(
    modifier: Modifier,
    events: List<Evento>,
    onAddEventClick: () -> Unit,
    onEventClick: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEventClick,
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(FAB_CORNER_RADIUS.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopBar(Modifier.padding(bottom = TOP_BAR_BOTTOM_PADDING.dp))
            when (events.isEmpty()) {
                false -> {
                    EventList(events, onEventClick)
                }
                true -> {
                    ErrorState()
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Event List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = { /* Handle search icon click */ }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
        IconButton(
            onClick = { /* Handle filter icon click */ }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Filter"
            )
        }
    }
}

@Composable
private fun EventList(
    events: List<Evento>,
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LIST_SPACING.dp)
    ) {
        items(events) {
            EventListItem(
                event = it,
                modifier = Modifier.clickable { onEventClick(it.id) }
            )
        }
    }
}

@Composable
private fun ErrorState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = Color.White,
            modifier = Modifier
                .background(color = Color(0xFF49454F), shape = CircleShape)
                .padding(ERROR_ICON_PADDING.dp)
        )
        Text(
            text = "Error",
            modifier = Modifier
                .padding(top = ERROR_TITLE_TOP_PADDING.dp)
                .padding(bottom = ERROR_TITLE_BOTTOM_PADDING.dp)
        )
        Text(
            text = "An error has occurred, please try again later",
            modifier = Modifier
                .padding(bottom = ERROR_MESSAGE_BOTTOM_PADDING.dp)
        )
        TextButton(
            onClick = { /* Handle retry click */ },
            shape = RoundedCornerShape(ERROR_BUTTON_CORNER_RADIUS.dp),
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color.Red,
                disabledContainerColor = Color.Red,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.width(ERROR_BUTTON_WIDTH.dp)
        ) {
            Text(text = "Try again")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListScreenPreview() {
    EventListScreen(
        modifier = Modifier,
        events = List(10) { index ->
            Evento(
                name = "Evento ${index + 1}",
                date = java.util.Date(System.currentTimeMillis()),
                id = "$index",
                attachedUser = com.example.eventorias.model.User(
                    name = "User ${index + 1}",
                    id = index.toLong(),
                    profilePicture = "image",
                )
            )
        },
        onAddEventClick = {},
        onEventClick = {}
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF1D1B20
)
@Composable
private fun EmptyEventListScreenPreview() {
    EventListScreen(
        modifier = Modifier,
        events = emptyList(),
        onAddEventClick = {},
        onEventClick = {}
    )
}

const val FAB_CORNER_RADIUS = 16
const val TOP_BAR_BOTTOM_PADDING = 12
const val LIST_SPACING = 8
const val ERROR_ICON_PADDING = 16
const val ERROR_TITLE_TOP_PADDING = 24
const val ERROR_TITLE_BOTTOM_PADDING = 5
const val ERROR_MESSAGE_BOTTOM_PADDING = 20
const val ERROR_BUTTON_CORNER_RADIUS = 5
const val ERROR_BUTTON_WIDTH = 150
