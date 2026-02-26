package com.example.eventorias.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_ui.components.DebouncedTextField
import com.example.core_ui.components.TextField
import com.example.core_ui.utils.photoPickerButtonAction
import com.example.eventorias.R
import com.example.eventorias.ui.components.RedToggle
import com.example.eventorias.ui.model.ProfileUiState
import com.example.eventorias.ui.viewmodel.UserProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var localImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            localImageUri = it
            viewModel.updateProfilePhoto(it.toString())
        }
    }

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    LaunchedEffect(notificationPermissionState?.status) {
        val isGranted = notificationPermissionState?.status?.isGranted ?: true
        viewModel.setNotificationsEnabled(isGranted)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize(),
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                TopBar(
                    profileImageUri = localImageUri,
                    profilePhotoUrl = null,
                    onImageClick = {
                        photoPickerButtonAction(photoPickerLauncher)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
                }
            }

            is ProfileUiState.Success -> {
                TopBar(
                    profileImageUri = localImageUri,
                    profilePhotoUrl = state.profilePhotoUrl,
                    onImageClick = { photoPickerButtonAction(photoPickerLauncher) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DebouncedTextField(
                    label = "Name",
                    value = state.user.displayName ?: "No name",
                    onInputValidated = { viewModel.updateUserName(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    label = "Email",
                    value = state.user.email ?: "No email",
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RedToggle(
                        checked = state.notificationsEnabled,
                        onCheckedChange = {
                            if (it) {
                                if (notificationPermissionState != null) {
                                    notificationPermissionState.launchPermissionRequest()
                                } else {
                                    viewModel.setNotificationsEnabled(true)
                                }
                            } else {
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                                context.startActivity(intent)
                            }
                        }
                    )
                    Text(
                        text = "Notifications",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            is ProfileUiState.Error -> {
                TopBar(
                    profileImageUri = localImageUri,
                    profilePhotoUrl = null,
                    onImageClick = {
                        photoPickerButtonAction(photoPickerLauncher)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    profileImageUri: Uri?,
    profilePhotoUrl: String?,
    onImageClick: () -> Unit,
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

        val imageModel = profileImageUri ?: profilePhotoUrl

        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onImageClick() }
            )
        } else {
            Image(
                painter = painterResource(R.drawable.auth_google_icon),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onImageClick() }
            )
        }
    }
}

@Preview
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}
