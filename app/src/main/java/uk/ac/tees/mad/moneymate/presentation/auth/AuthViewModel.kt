package uk.ac.tees.mad.moneymate.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.repo.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    var authState by mutableStateOf(AuthState())

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

    fun isAuthenticated(): Boolean {
        return authRepository.isUserAuthenticated()
    }

    fun signOut() {
        authRepository.signOut()
        authState = authState.copy(isAuthenticated = false)
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)








