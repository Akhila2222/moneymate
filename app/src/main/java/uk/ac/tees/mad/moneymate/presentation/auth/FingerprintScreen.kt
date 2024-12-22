package uk.ac.tees.mad.moneymate.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun FingerprintScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState = viewModel.authState
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.authenticateWithFingerprint(context as FragmentActivity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fingerprint Authentication",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (!authState.isAuthenticated) {
            Text("Please authenticate using your fingerprint")
        } else {
            Text("Authentication successful", color = MaterialTheme.colorScheme.primary)
            LaunchedEffect(Unit) {
                navController.navigate("dashboard_screen") {
                    popUpTo("fingerprint_auth") { inclusive = true }
                }
            }
        }

        if (authState.errorMessage != null) {

            Text(
                "Authentication error: ${authState.errorMessage}",
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = { viewModel.authenticateWithFingerprint(context as FragmentActivity) }) {
                Text("Retry")
            }
        }

        if (authState.fingerprintNotAvailable != null) {
            Text(
                "Fingerprint authentication not available",
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = {
                navController.navigate("login_screen") {
                    popUpTo("fingerprint_auth") { inclusive = true }
                }
            }) {
                Text("Continue")
            }

        }
    }
}