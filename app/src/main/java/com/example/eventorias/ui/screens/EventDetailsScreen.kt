package com.example.eventorias.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.eventorias.R
import com.example.eventorias.ui.model.EventDetailsUiState
import kotlinx.serialization.Serializable

@Serializable
data object EventDetailsScreen : NavKey

@Composable
fun EventDetailsScreen(
    uiState: EventDetailsUiState,
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1C22))
            .verticalScroll(scrollState)
            .padding(horizontal = SCREEN_PADDING_HORIZONTAL.dp)
    ) {
        Spacer(Modifier.height(HEADER_TOP_SPACER.dp))

        EventDetailsScreenBody(title = uiState.title, onBack = onBack)

        Spacer(Modifier.height(IMAGE_TOP_SPACER.dp))

        Image(
            painter = painterResource(id = uiState.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(MAIN_IMAGE_HEIGHT.dp)
                .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(DATES_TOP_SPACER.dp))

        EventDatesComponent(
            date = uiState.date,
            time = uiState.time,
            authorImageUrl = uiState.authorImageUrl
        )

        Spacer(Modifier.height(DESCRIPTION_TOP_SPACER.dp))

        Text(
            text = uiState.description,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(ADDRESS_TOP_SPACER.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(ADDRESS_ROW_SPACING.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = uiState.address,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )

            AsyncImage(
                model = uiState.mapImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MAP_HEIGHT.dp)
                    .width(MAP_WIDTH.dp)
                    .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun EventDatesComponent(
    date: String,
    time: String,
    authorImageUrl: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.spacedBy(DATE_ROW_SPACING.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(DATE_ICON_SPACING.dp))
                Text(
                    text = date,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(DATE_ICON_SPACING.dp))
                Text(
                    text = time,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = authorImageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(AUTHOR_IMAGE_SIZE.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun EventDetailsScreenBody(
    title: String,
    onBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(BACK_ICON_SIZE.dp)
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBack() }
        )
        Spacer(Modifier.width(TITLE_SPACING.dp))
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1C22)
@Composable
fun EventDetailsPreview() {
    val sampleState = EventDetailsUiState(
        title = "Art exhibition",
        description = "Join us for an exclusive Art Exhibition showcasing the "
                + "works of a talented contemporary artist. This exhibition "
                + "features a captivating collection of both modern and classical "
                + "pieces, offering a unique insight into the creative journey. "
                + "Whether you're an art enthusiast or a casual visitor, you'll "
                + "have the chance to explore a diverse range of artworks.",
        address = "123 Rue de l'Art,\nQuartier des Galeries,\nParis, 75003, France",
        date = "July 20, 2024",
        time = "10:00 AM",
        imageUrl = R.drawable.ic_launcher_background,
        mapImageUrl = "https://maps.googleapis.com/maps/api/staticmap?center=48.8566,2.3522&zoom=15&size=600x300&maptype=roadmap&markers=color:red%7C48.8566,2.3522",
        authorImageUrl = R.drawable.auth_google_icon
    )
    EventDetailsScreen(uiState = sampleState)
}

private const val SCREEN_PADDING_HORIZONTAL = 16
private const val HEADER_TOP_SPACER = 16
private const val IMAGE_TOP_SPACER = 20
private const val MAIN_IMAGE_HEIGHT = 360
private const val IMAGE_CORNER_RADIUS = 12
private const val DATES_TOP_SPACER = 24
private const val DESCRIPTION_TOP_SPACER = 24
private const val ADDRESS_TOP_SPACER = 48
private const val ADDRESS_ROW_SPACING = 16
private const val MAP_HEIGHT = 72
private const val MAP_WIDTH = 149
private const val DATE_ROW_SPACING = 8
private const val DATE_ICON_SPACING = 8
private const val AUTHOR_IMAGE_SIZE = 60
private const val BACK_ICON_SIZE = 26
private const val TITLE_SPACING = 8
