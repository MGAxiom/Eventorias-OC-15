package com.example.eventorias.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    LabelContent(
        label = label,
        value = value,
        placeholder = placeholder,
        onValueChange = onValueChange,
        enabled = true,
        readOnly = false,
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier
    )
}

@Preview
@Composable
private fun LabeledTextFieldPreview() {
    TextField(
        label = "Name",
        value = "",
        placeholder = "Christopher Evans"
    )
}
