package com.example.core_ui.components

import android.app.DatePickerDialog
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
fun DateTextField(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit,
    placeholder: String = "",
    readOnly: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate =
                    "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
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
        ) { datePickerDialog.show() }
    )
}
