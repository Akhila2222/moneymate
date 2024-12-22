package uk.ac.tees.mad.moneymate.presentation.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.coroutines.delay
import uk.ac.tees.mad.moneymate.R
import uk.ac.tees.mad.moneymate.presentation.auth.AuthViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isFingerprintEnabled by authViewModel.fingerprintSetting.collectAsState()
    // Launch effect for splash screen delay
    LaunchedEffect(key1 = true) {
        delay(3000L) // Delay for 3 seconds
        val isAuthenticated = Firebase.auth.currentUser != null
        Log.d("SplashScreen", "isAuthenticated: $isFingerprintEnabled")
        val destination =
            if (isAuthenticated && isFingerprintEnabled) "fingerprint_auth" else if (isAuthenticated) "dashboard_screen" else "login_screen"
        navController.navigate(destination) {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    // Splash screen UI
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.moneymate_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            BasicText(
                text = "Moneymate",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}