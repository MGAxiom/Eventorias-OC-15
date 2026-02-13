package com.example.core_ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eventorias.core.components.LabelContent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun DebouncedTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeholder: String = "",
    onInputValidated: (String) -> Unit = {},
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        if (searchText != value) {
            searchText = value
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { searchText }
            .debounce(1500)
            .distinctUntilChanged()
            .collect { query ->
                if (query != value) {
                    onInputValidated(query)
                }
            }
    }

    LabelContent(
        label = label,
        value = searchText,
        placeholder = placeholder,
        onValueChange = { searchText = it },
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
    DebouncedTextField(
        label = "Name",
        value = "",
        placeholder = "Christopher Evans"
    )
}
