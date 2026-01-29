package team.international2c.pvz2c_level_editor.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import team.international2c.pvz2c_level_editor.dataStore


class SettingsRepository(private val context: Context) {

    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit {
            it[isDarkThemeKey] = isDarkTheme
        }
    }
}
