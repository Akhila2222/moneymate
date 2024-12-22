package uk.ac.tees.mad.moneymate.presentation.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.repo.AuthenticationRepository
import uk.ac.tees.mad.moneymate.utils.BiometricAuthHelper
import uk.ac.tees.mad.moneymate.utils.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val biometricAuthHelper: BiometricAuthHelper,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    var authState by mutableStateOf(AuthState())

    val fingerprintSetting = preferencesManager.isFingerprintEnabled.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )
    val themeSetting = preferencesManager.themeSetting.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authState = authState.copy(isLoading = true)
            val result = authRepository.signInWithEmailPassword(email, password)
            authState = if (result != null) {
                authState.copy(isLoading = false, isAuthenticated = true)
            } else {
                authState.copy(isLoading = false, errorMessage = "Authentication failed")
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            authState = authState.copy(isLoading = true)
            val result = authRepository.signUpWithEmailPassword(name, email, password)
            authState = if (result != null) {
                authState.copy(isLoading = false, isAuthenticated = true)
            } else {
                authState.copy(isLoading = false, errorMessage = "Registration failed")
            }
        }
    }

    fun authenticateWithFingerprint(context: FragmentActivity) {
        viewModelScope.launch {
            if (!preferencesManager.isFingerprintEnabled.first()) {
                authState = authState.copy(fingerprintNotAvailable = true)
                return@launch
            }

            if (biometricAuthHelper.canAuthenticate()) {
                biometricAuthHelper.authenticate(
                    onSuccess = {
                        authState = authState.copy(isAuthenticated = true)
                    },
                    onError = { error ->
                        authState = authState.copy(errorMessage = error)
                    },
                    onFailed = {
                        authState = authState.copy(errorMessage = "Unknown error")
                    },
                    fContext = context
                )
            } else {
                authState = authState.copy(fingerprintNotAvailable = true)
            }
        }
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val fingerprintNotAvailable: Boolean? = null
)








