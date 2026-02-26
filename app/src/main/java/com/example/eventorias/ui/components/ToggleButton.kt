package com.example.eventorias.ui.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RedToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Color(0xFFE60012),
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xFFBFBFBF),
        )
    )
}

@Preview
@Composable
fun RedTooglePreview() {
    RedToggle(checked = true, onCheckedChange = {})
}