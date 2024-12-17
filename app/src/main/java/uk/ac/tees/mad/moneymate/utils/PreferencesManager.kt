package uk.ac.tees.mad.moneymate.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val dataStore: DataStore<Preferences>) {


    val themeSetting: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE] ?: false
    }

    val isFingerprintEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FINGERPRINT_ENABLED] ?: false
    }

    suspend fun toggleTheme() {
        dataStore.edit { preferences ->

            val isDarkMode = preferences[PreferencesKeys.THEME_MODE] ?: false
            preferences[PreferencesKeys.THEME_MODE] = !isDarkMode
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
    val THEME_MODE = booleanPreferencesKey("theme_mode_dark")
    val FINGERPRINT_ENABLED = booleanPreferencesKey("fingerprint_enabled")
}
