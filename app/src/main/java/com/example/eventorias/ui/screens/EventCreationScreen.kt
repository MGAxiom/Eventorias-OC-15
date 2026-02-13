package com.example.eventorias.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.eventorias.BuildConfig
import com.example.eventorias.R
import com.example.eventorias.core.components.DateTextField
import com.example.eventorias.core.components.TextField
import com.example.core_ui.components.TimeTextField
import com.example.core_ui.utils.createImageFile
import com.example.core_ui.utils.photoButtonAction
import com.example.core_ui.utils.photoPickerButtonAction
import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime
import com.example.eventorias.ui.model.EventUiState
import com.example.eventorias.ui.model.FormEvent
import com.example.eventorias.ui.viewmodel.EventViewModel
import java.io.File
import java.util.Objects
import org.koin.compose.koinInject

@Composable
fun EventCreationScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val event by viewModel.event.collectAsState()
    val validationErrors by viewModel.validationErrors.collectAsState()

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
                validationErrors = validationErrors,
                onFormEvent = viewModel::onAction
            )
            EventCreationButtons(
                photoUri = event.photoUri,
                onFormEvent = viewModel::onAction
            )
            EventCreationFooter(
                onConfirm = { viewModel.addEvent() },
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
    validationErrors: Map<String, String>,
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
            isError = validationErrors.containsKey("title"),
            errorMessage = validationErrors["title"],
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            label = "Description",
            value = description,
            onValueChange = { onFormEvent(FormEvent.DescriptionChanged(it)) },
            placeholder = "Tap here to enter your description",
            isError = validationErrors.containsKey("description"),
            errorMessage = validationErrors["description"],
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
                isError = validationErrors.containsKey("date"),
                errorMessage = validationErrors["date"],
                modifier = Modifier.weight(1f)
            )
            TimeTextField(
                label = "Time",
                value = time,
                onTimeSelected = { onFormEvent(FormEvent.TimeChanged(it)) },
                placeholder = "HH : MM",
                isError = validationErrors.containsKey("time"),
                errorMessage = validationErrors["time"],
                modifier = Modifier.weight(1f)
            )
        }
        TextField(
            label = "Address",
            value = address,
            onValueChange = { onFormEvent(FormEvent.LocationChanged(it)) },
            placeholder = "Enter full adress",
            isError = validationErrors.containsKey("address"),
            errorMessage = validationErrors["address"],
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ColumnScope.EventCreationButtons(
    photoUri: String?,
    onFormEvent: (FormEvent) -> Unit
) {
    val context = LocalContext.current
    val file = remember { createImageFile(context) }
    val uri = remember(file) {
        FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider", file
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onFormEvent(FormEvent.PhotoUriChanged(uri.toString()))
            }
        }
    )

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { resultUri ->
        resultUri?.let { onFormEvent(FormEvent.PhotoUriChanged(it.toString())) }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { photoButtonAction(cameraLauncher, uri) },
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
            onClick = { photoPickerButtonAction(pickMedia) },
            shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(54.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.attach_file_icon),
                contentDescription = "Add file",
            )
        }
    }

    if (photoUri != null) {
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = "Selected photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    } else {
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun EventCreationFooter(
    onConfirm: () -> Unit
) {
    TextButton(
        onClick = onConfirm,
        shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red, contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .padding(bottom = 14.dp)
            .padding(horizontal = 24.dp)
            .height(54.dp)
            .fillMaxWidth()
    ) {
        Text("Validate")
    }
}

@Preview
@Composable
fun EventCreationScreenPreview() {
    // Preview won't work with koinInject, but it's okay for now.
    // To fix, we would pass a fake ViewModel.
    // EventCreationScreen(onBack = {})
}