package com.example.eventorias.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventorias.R
import com.example.eventorias.model.Evento
import com.example.eventorias.model.User
import java.util.Date

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
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                // Changed padding to only apply to the start (left), so the end (right) has 0 padding.
                .padding(start = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.auth_google_icon),
                contentDescription = "profile image",
                modifier = Modifier
                    .size(40.dp)
                    .fillMaxHeight()
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
                    text = event.date.time.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "event image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(136.dp)
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
            date = Date(
                System.currentTimeMillis()
            ),
            id = "1",
            attachedUser = User(
                name = "User 1",
                id = 1,
                profilePicture = "image",
            )
        )
    )
}
