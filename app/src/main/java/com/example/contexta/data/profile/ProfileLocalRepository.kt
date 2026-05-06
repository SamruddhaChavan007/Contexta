package com.example.contexta.data.profile

import com.example.contexta.profile.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileLocalRepository {
    val profileFlow: Flow<Profile?>
    suspend fun saveProfile(profile: Profile)
    suspend fun clearProfile()
}
