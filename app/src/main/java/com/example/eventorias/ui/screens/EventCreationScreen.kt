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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventorias.ui.components.LabeledValueField

@Composable
fun EventCreationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EventCreationHeader(onBack)
            EventCreationBody()
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
private fun EventCreationBody() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        LabeledValueField(
            label = "Title",
            value = "New event",
            modifier = Modifier.fillMaxWidth()
        )
        LabeledValueField(
            label = "Description",
            value = "Tap here to enter your description",
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LabeledValueField(
                label = "Date",
                value = "MM/DD/YYYY",
                modifier = Modifier.weight(1f)
            )
            LabeledValueField(
                label = "Time",
                value = "HH : MM",
                modifier = Modifier.weight(1f)
            )
        }
        LabeledValueField(
            label = "Address",
            value = "Enter full adress",
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
                imageVector = Icons.Default.Add,
                contentDescription = "Cancel",
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
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
            )
        }
    }
}

@Preview
@Composable
fun EventCreationScreenPreview() {
    EventCreationScreen()
}
