package com.example.contexta.data.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")
private val THEME_KEY = intPreferencesKey("theme_preference")

class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemeRepository {

    override val themePreference: Flow<ThemePreference> = context.themeDataStore.data
        .map { prefs -> ThemePreference.fromOrdinal(prefs[THEME_KEY] ?: ThemePreference.SYSTEM.ordinal) }

    override suspend fun setThemePreference(pref: ThemePreference) {
        context.themeDataStore.edit { it[THEME_KEY] = pref.ordinal }
    }
}
