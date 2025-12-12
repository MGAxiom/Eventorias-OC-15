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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventorias.R
import com.example.eventorias.core.components.LabeledValueField

@Composable
fun EventCreationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EventCreationHeader(onBack)
            EventCreationBody(
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it },
                date = date,
                onDateChange = { date = it },
                time = time,
                onTimeChange = { time = it },
                address = address,
                onAddressChange = { address = it }
            )
            EventCreationButtons(onCancel = onBack)
        }
        TextButton(
            onClick = { /* Handle validate click */ },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Red,
                disabledContentColor = Color.White,
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = "Validate",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}

@Composable
private fun EventCreationBody(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        LabeledValueField(
            label = "Title",
            value = title,
            onValueChange = onTitleChange,
            placeholder = "New event",
            modifier = Modifier.fillMaxWidth()
        )
        LabeledValueField(
            label = "Description",
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = "Tap here to enter your description",
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LabeledValueField(
                label = "Date",
                value = date,
                onValueChange = onDateChange,
                placeholder = "MM/DD/YYYY",
                modifier = Modifier.weight(1f)
            )
            LabeledValueField(
                label = "Time",
                value = time,
                onValueChange = onTimeChange,
                placeholder = "HH : MM",
                modifier = Modifier.weight(1f)
            )
        }
        LabeledValueField(
            label = "Address",
            value = address,
            onValueChange = onAddressChange,
            placeholder = "Enter full adress",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EventCreationButtons(onCancel: () -> Unit) {
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
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black,
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
            onClick = { /* Handle confirm click */ },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Red,
                disabledContentColor = Color.White,
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
}

@Preview
@Composable
fun EventCreationScreenPreview() {
    EventCreationScreen()
}
