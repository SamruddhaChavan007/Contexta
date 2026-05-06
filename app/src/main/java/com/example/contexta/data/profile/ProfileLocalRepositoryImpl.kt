package com.example.contexta.data.profile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.contexta.profile.data.model.Profile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_prefs")

private val KEY_ID = stringPreferencesKey("profile_id")
private val KEY_FULL_NAME = stringPreferencesKey("profile_full_name")
private val KEY_EMAIL = stringPreferencesKey("profile_email")
private val KEY_CREATED_AT = stringPreferencesKey("profile_created_at")

class ProfileLocalRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileLocalRepository {

    override val profileFlow: Flow<Profile?> = context.profileDataStore.data.map { prefs ->
        val id = prefs[KEY_ID] ?: return@map null
        Profile(
            id = id,
            fullName = prefs[KEY_FULL_NAME],
            email = prefs[KEY_EMAIL] ?: return@map null,
            created_at = prefs[KEY_CREATED_AT] ?: return@map null
        )
    }

    override suspend fun saveProfile(profile: Profile) {
        context.profileDataStore.edit { prefs ->
            prefs[KEY_ID] = profile.id
            prefs[KEY_FULL_NAME] = profile.fullName ?: ""
            prefs[KEY_EMAIL] = profile.email
            prefs[KEY_CREATED_AT] = profile.created_at
        }
    }

    override suspend fun clearProfile() {
        context.profileDataStore.edit { prefs ->
            prefs.remove(KEY_ID)
            prefs.remove(KEY_FULL_NAME)
            prefs.remove(KEY_EMAIL)
            prefs.remove(KEY_CREATED_AT)
        }
    }
}
