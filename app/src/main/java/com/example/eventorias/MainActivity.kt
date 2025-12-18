package com.example.eventorias

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.eventorias.ui.navigation.Screen
import com.example.eventorias.ui.screens.EventCreationScreen
import com.example.eventorias.ui.screens.EventDetailsScreen
import com.example.eventorias.ui.screens.EventDetailsUiState
import com.example.eventorias.ui.screens.MainScreen
import com.example.eventorias.ui.theme.EventoriasTheme
import com.example.network.GoogleMapsStaticRepository
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
                                    EventCreationScreen(
                                        onBack = { backStack.removeLastOrNull() }
                                    )
                                }
                                is Screen.EventDetails -> NavEntry(key) {
                                    val address = "123 Rue de l'Art,\nQuartier des Galeries,\nParis, 75003, France"
                                    val googleMapsRepository = koinInject<GoogleMapsStaticRepository>()
                                    val mapUrl = googleMapsRepository.getStaticMapUrl(address = address)
                                    
                                    val dummyState = EventDetailsUiState(
                                        title = "Art exhibition ${key.eventId}",
                                        description = "Join us for an exclusive Art Exhibition showcasing the "
                                                + "works of a talented contemporary artist. This exhibition "
                                                + "features a captivating collection of both modern and classical "
                                                + "pieces, offering a unique insight into the creative journey. "
                                                + "Whether you're an art enthusiast or a casual visitor, you'll "
                                                + "have the chance to explore a diverse range of artworks.",
                                        address = address,
                                        date = "July 20, 2024",
                                        time = "10:00 AM",
                                        imageUrl = R.drawable.ic_launcher_background,
                                        mapImageUrl = mapUrl,
                                        authorImageUrl = R.drawable.auth_google_icon
                                    )
                                    EventDetailsScreen(
                                        uiState = dummyState,
                                        onBack = { backStack.removeLastOrNull() }
                                    )
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
