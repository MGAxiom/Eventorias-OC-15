package com.example.eventorias.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EventListScreen() {
    TopBar()
}

@Composable
private fun TopBar() {
    Row {
        Text(
            text = "Event List",
            style = MaterialTheme.typography.headlineMedium
        )
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
                imageVector = Icons.Default.Menu ,
                contentDescription = "Filter"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListScreenPreview() {
    EventListScreen()
}