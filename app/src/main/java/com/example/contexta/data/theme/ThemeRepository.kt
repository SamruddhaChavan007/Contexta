package com.example.contexta.data.theme

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val themePreference: Flow<ThemePreference>
    suspend fun setThemePreference(pref: ThemePreference)
}
