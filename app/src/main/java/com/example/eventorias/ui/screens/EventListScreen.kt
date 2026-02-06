package com.example.eventorias.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Evento
import com.example.domain.model.User
import com.example.eventorias.ui.components.EventListItem

@Composable
fun EventListScreen(
    modifier: Modifier = Modifier,
    events: List<Evento>,
    onEventClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxSize()
    ) {
        if (events.isNotEmpty()) {
            EventList(events, onEventClick)
        } else {
            EmptyState()
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
        items(events, key = { it.id }) { item ->
            EventListItem(
                event = item,
                modifier = Modifier.clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onEventClick(item.id) }
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No events yet")
        Text(
            text = "Create your first event!",
            modifier = Modifier.padding(top = 8.dp)
        )
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
                date = System.currentTimeMillis(),
                id = "$index",
                attachedUser = User(
                    displayName = "User ${index + 1}",
                    uid = index.toString(),
                    photoUrl = null,
                    email = "this@email.com"
                ),
                description = "",
                photoUrl = "",
                location = ""
            )
        },
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
        onEventClick = {}
    )
}

const val LIST_SPACING = 8