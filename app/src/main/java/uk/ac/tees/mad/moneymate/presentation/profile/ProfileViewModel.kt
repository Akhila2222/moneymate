package uk.ac.tees.mad.moneymate.presentation.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.utils.PreferencesManager
import uk.ac.tees.mad.moneymate.repo.UserProfile
import uk.ac.tees.mad.moneymate.repo.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfile())
    val uiState = _uiState.asStateFlow()


    fun getUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile().collect {
                _uiState.value = it
            }
        }
    }

    init {
        getUserProfile()
    }

    fun changeUserProfile(user: UserProfile) {
        _uiState.value = user
    }

    val themeSetting = preferencesManager.themeSetting.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    val fingerprintSetting = preferencesManager.isFingerprintEnabled.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    var isLoading = mutableStateOf(false)
        private set

    fun updateUserProfile(user: UserProfile, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            repository.updateUserProfile(user) {
                onSuccess()
                isLoading.value = false
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            preferencesManager.toggleTheme()
        }
    }

    fun toggleFingerprint() {
        viewModelScope.launch {
            preferencesManager.toggleFingerprint()
        }
    }
}
