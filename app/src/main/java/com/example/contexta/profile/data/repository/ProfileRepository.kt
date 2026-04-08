package com.example.contexta.profile.data.repository

import com.example.contexta.profile.data.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val client: SupabaseClient
) {
    suspend fun getProfile(): Profile? {
        val userId = client.auth.currentUserOrNull()?.id ?: return null

        return client.postgrest["profiles"]
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<Profile>()
    }
}