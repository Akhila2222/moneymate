package uk.ac.tees.mad.moneymate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val dataStore: DataStore<Preferences>) {


    val themeSetting: Flow<ThemeMode> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE]?.let { ThemeMode.fromString(it) } ?: ThemeMode.LIGHT
    }

    val isFingerprintEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FINGERPRINT_ENABLED] ?: false
    }

    suspend fun toggleTheme() {
        dataStore.edit { preferences ->
            val currentMode = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.LIGHT
            preferences[PreferencesKeys.THEME_MODE] =
                if (currentMode == ThemeMode.LIGHT) ThemeMode.DARK.toString() else ThemeMode.LIGHT.toString()
        }
    }

    suspend fun toggleFingerprint() {
        dataStore.edit { preferences ->
            val isEnabled = preferences[PreferencesKeys.FINGERPRINT_ENABLED] ?: false
            preferences[PreferencesKeys.FINGERPRINT_ENABLED] = !isEnabled
        }
    }
}

object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val FINGERPRINT_ENABLED = booleanPreferencesKey("fingerprint_enabled")
}

enum class ThemeMode {
    LIGHT,
    DARK;

    companion object {
        fun fromString(mode: String?): ThemeMode {
            return when (mode) {
                "DARK" -> DARK
                else -> LIGHT
            }
        }
    }
}


