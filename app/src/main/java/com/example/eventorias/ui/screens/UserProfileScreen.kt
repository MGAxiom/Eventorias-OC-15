package com.example.eventorias.ui.screens

import android.widget.ToggleButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.example.eventorias.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventorias.core.components.LabeledValueField
import com.example.eventorias.ui.components.RedToggle

@Composable
fun UserProfileScreen() {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp )
            .fillMaxSize(),
    ) {
        TopBar(
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LabeledValueField(
            label = "Name",
            value = "Christopher Evans",
            modifier = Modifier.fillMaxWidth()
        )
        LabeledValueField(
            label = "Email",
            value = "william.henry.moody@my-own-personal-domain.com",
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RedToggle()
            Text(
                text = "Notifications",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "User Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.auth_google_icon),
            contentDescription = "Search",
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}