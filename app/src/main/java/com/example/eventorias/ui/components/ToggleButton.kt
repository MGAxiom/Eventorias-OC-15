package com.example.eventorias.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RedToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor = if (checked) Color(0xFFE60012) else Color(0xFFBFBFBF)
    val alignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .align(alignment)
                .background(Color.White, shape = CircleShape)
        )
    }
}