package com.example.eventorias.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.example.eventorias.R
import com.example.eventorias.core.components.DateTextField
import com.example.eventorias.core.components.TextField
import com.example.eventorias.core.components.TimeTextField
import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.example.eventorias.ui.viewmodel.EventViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
data object EventCreationScreenKey : NavKey

@Composable
fun EventCreationScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val event by viewModel.event.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EventCreationHeader(onBack)
            EventCreationBody(
                title = event.name,
                description = event.description,
                date = formatDate(event.date),
                time = formatTime(event.date),
                address = event.location,
                onFormEvent = viewModel::onAction
            )
            EventCreationButtons(
                onCancel = onBack,
                onConfirm = { viewModel.addEvent() }
            )
        }

        if (uiState == EventUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun EventCreationHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
            )
        }
    }
}

@Composable
private fun EventCreationBody(
    onFormEvent: (FormEvent) -> Unit,
    title: String,
    description: String,
    date: String,
    time: String,
    address: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        TextField(
            label = "Title",
            value = title,
            onValueChange = { onFormEvent(FormEvent.TitleChanged(it)) },
            placeholder = "New event",
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            label = "Description",
            value = description,
            onValueChange = { onFormEvent(FormEvent.DescriptionChanged(it)) },
            placeholder = "Tap here to enter your description",
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()
        ) {
            DateTextField(
                label = "Date",
                value = date,
                onDateSelected = { onFormEvent(FormEvent.DateChanged(it)) },
                placeholder = "MM/DD/YYYY",
                modifier = Modifier.weight(1f)
            )
            TimeTextField(
                label = "Time",
                value = time,
                onTimeSelected = { onFormEvent(FormEvent.TimeChanged(it)) },
                placeholder = "HH : MM",
                modifier = Modifier.weight(1f)
            )
        }
        TextField(
            label = "Address",
            value = address,
            onValueChange = { onFormEvent(FormEvent.LocationChanged(it)) },
            placeholder = "Enter full adress",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EventCreationButtons(onCancel: () -> Unit, onConfirm: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onCancel,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, contentColor = Color.Black
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(54.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_buton_photo),
                contentDescription = "Add Photo",
            )
        }
        Button(
            onClick = onConfirm,
            shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            ), contentPadding = PaddingValues(0.dp), modifier = Modifier.size(54.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.attach_file_icon),
                contentDescription = "Add file",
            )
        }
    }
}

@Preview
@Composable
fun EventCreationScreenPreview() {
    // Preview won't work with koinInject, but it's okay for now.
    // To fix, we would pass a fake ViewModel.
    // EventCreationScreen(onBack = {})
}
