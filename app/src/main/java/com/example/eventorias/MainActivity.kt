package com.example.eventorias

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.eventorias.core.utils.formatDate
import com.example.eventorias.core.utils.formatTime
import com.example.eventorias.ui.model.EventDetailsUiState
import com.example.eventorias.ui.navigation.Screen
import com.example.eventorias.ui.screens.EventCreationScreen
import com.example.eventorias.ui.screens.EventDetailsScreen
import com.example.eventorias.ui.screens.MainScreen
import com.example.eventorias.ui.theme.EventoriasTheme
import com.example.eventorias.ui.viewmodel.EventViewModel
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventoriasTheme {
                val backStack = remember { mutableStateListOf<Any>(Screen.Login) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is Screen.Login -> NavEntry(key) {
                                    AuthScreen(
                                        onLoginSuccess = {
                                            backStack.clear()
                                            backStack.add(Screen.Main)
                                        }
                                    )
                                }
                                is Screen.Main -> NavEntry(key) {
                                    MainScreen(
                                        onNavigateToEventCreation = {
                                            backStack.add(Screen.EventCreation)
                                        },
                                        onNavigateToEventDetails = { eventId ->
                                            backStack.add(Screen.EventDetails(eventId))
                                        }
                                    )
                                }
                                is Screen.EventCreation -> NavEntry(key) {
                                    val viewModel: EventViewModel = koinInject()
                                    val eventSaved by viewModel.eventSaved.collectAsState()

                                    LaunchedEffect(eventSaved) {
                                        if (eventSaved) {
                                            viewModel.onSaveComplete()
                                            backStack.removeLastOrNull()
                                        }
                                    }

                                    EventCreationScreen(
                                        onBack = { backStack.removeLastOrNull() },
                                    )
                                }
                                is Screen.EventDetails -> NavEntry(key) {
                                    val viewModel: EventViewModel = koinInject()

                                    // Fetch the specific event
                                    LaunchedEffect(key.eventId) {
                                        viewModel.getEventById(key.eventId)
                                    }

                                    val selectedEvent by viewModel.selectedEvent.collectAsState() // CHANGED

                                    selectedEvent?.let { event ->
                                        val mapUrl = viewModel.getMapUrl(address = event.location)

                                        val uiState = EventDetailsUiState(
                                            title = event.name,
                                            description = event.description,
                                            address = event.location,
                                            date = formatDate(event.date),
                                            time = formatTime(event.date),
                                            imageUrl = event.photoUrl ?: "",
                                            mapImageUrl = mapUrl,
                                            authorImageUrl = event.attachedUser?.photoUrl
                                        )

                                        EventDetailsScreen(
                                            uiState = uiState,
                                            onBack = { backStack.removeLastOrNull() }
                                        )
                                    } ?: run {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                                else -> NavEntry(Unit) { }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val user = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract(),
    ) { res ->
        val response = res.idpResponse
        if (res.resultCode == Activity.RESULT_OK) {
            user.value = FirebaseAuth.getInstance().currentUser
            Toast.makeText(context, "Welcome ${user.value?.displayName}", Toast.LENGTH_SHORT).show()
        } else {
            val errorMessage = response?.error?.message ?: "Sign in cancelled"
            Toast.makeText(context, "Sign in failed: $errorMessage", Toast.LENGTH_LONG).show()
        }
    }

    if (user.value == null) {
        LaunchedEffect(Unit) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val customAuthLayout = AuthMethodPickerLayout
                .Builder(R.layout.auth_method_picker)
                .setGoogleButtonId(R.id.google_button)
                .setEmailButtonId(R.id.email_button)
                .build()

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_Eventorias_Auth)
                .setAuthMethodPickerLayout(customAuthLayout)
                .build()
            signInLauncher.launch(signInIntent)
        }
    } else {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }
}
