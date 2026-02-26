package com.example.eventorias.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.Evento
import com.example.domain.model.User
import com.example.eventorias.R
import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime

@Composable
fun EventListItem(
    event: Evento,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            AsyncImage(
                model = event.attachedUser?.photoUrl ?: R.drawable.auth_google_icon,
                contentDescription = null, // Should not be announced
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = event.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = "${formatDate(event.date)} at ${formatTime(event.date)}",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AsyncImage(
                model = event.photoUrl ?: R.drawable.ic_launcher_background,
                contentDescription = null, // Should not be announced
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(136.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(5.dp))
            )
        }
    }
}

@Composable
@Preview
private fun EventListItemPreview() {
    EventListItem(
        event = Evento(
            name = "Evento 1",
            date = System.currentTimeMillis(),
            id = "1",
            attachedUser = User(
                uid = "1",
                displayName = "User 1",
                email = "this@email.com",
                photoUrl = null,
            ),
            description = "",
            photoUrl = "",
            location = ""
        )
    )
}
