package com.example.contexta.profile.data.repository

import com.example.contexta.profile.data.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile?
    suspend fun clearProfile()
}
