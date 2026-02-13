package com.example.core_ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.eventorias.core.components.LabelContent
import java.util.Calendar

@Composable
fun TimeTextField(
    label: String,
    value: String,
    onTimeSelected: (String) -> Unit,
    placeholder: String = "",
    is24Hour: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
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
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier.clickable(
            indication = LocalIndication.current,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            timePickerDialog.show()
        }
    )
}
