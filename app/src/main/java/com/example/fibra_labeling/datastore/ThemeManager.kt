package com.example.fibra_labeling.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit

class ThemeManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    private val _themeMode = mutableStateOf(getSavedTheme())
    val themeMode: State<ThemeMode> = _themeMode

    fun setTheme(theme: ThemeMode) {
        _themeMode.value = theme
        saveTheme(theme)
    }

    private fun getSavedTheme(): ThemeMode {
        val themeName = prefs.getString("selected_theme", "LIGHT")
        return try {
            ThemeMode.valueOf(themeName ?: "LIGHT")
        } catch (e: IllegalArgumentException) {
            ThemeMode.LIGHT
        }
    }

    private fun saveTheme(theme: ThemeMode) {
        prefs.edit { putString("selected_theme", theme.name) }
    }

    fun getThemeDisplayName(theme: ThemeMode): String {
        return when (theme) {
            ThemeMode.LIGHT -> "Claro"
            ThemeMode.DARK -> "Oscuro"
            ThemeMode.SYSTEM -> "Automático"
        }
    }
}


enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}
