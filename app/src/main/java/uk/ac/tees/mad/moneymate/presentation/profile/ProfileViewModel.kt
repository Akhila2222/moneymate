package uk.ac.tees.mad.moneymate.presentation.profile

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.PreferencesManager
import uk.ac.tees.mad.moneymate.ThemeMode
import uk.ac.tees.mad.moneymate.repo.UserProfile
import uk.ac.tees.mad.moneymate.repo.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userProfile = repository.getUserProfile().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        UserProfile()
    )
    val themeSetting = preferencesManager.themeSetting.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ThemeMode.LIGHT
    )
    val fingerprintSetting = preferencesManager.isFingerprintEnabled.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    fun updateUserProfile(user: UserProfile) {
        viewModelScope.launch {
            repository.updateUserProfile(user)
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
