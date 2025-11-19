
package com.example.eventorias

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.R as AppCompatR
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.eventorias.ui.screens.EventListScreen
import com.example.eventorias.ui.theme.EventoriasTheme
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventoriasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
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

            // Configuration du layout personnalisé
            val customLayout = AuthMethodPickerLayout
                .Builder(R.layout.auth_method_picker)
                .setGoogleButtonId(R.id.google_button)
                .setEmailButtonId(R.id.email_button)
                .build()

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAuthMethodPickerLayout(customLayout) // Application du layout
                .setTheme(AppCompatR.style.Theme_AppCompat_DayNight_NoActionBar)
                .build()
            signInLauncher.launch(signInIntent)
        }
    } else {
        EventListScreen()
    }
}
