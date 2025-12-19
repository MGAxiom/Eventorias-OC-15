package com.example.eventorias.core.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun TimeTextField(
    label: String,
    value: String,
    onTimeSelected: (String) -> Unit,
    placeholder: String = "",
    is24Hour: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val formattedTime = "%02d:%02d".format(hour, minute)
                onTimeSelected(formattedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            is24Hour
        )
    }

    LabelContent(
        label = label,
        value = value,
        placeholder = placeholder,
        enabled = false,
        modifier = modifier.clickable { timePickerDialog.show() }
    )
}
