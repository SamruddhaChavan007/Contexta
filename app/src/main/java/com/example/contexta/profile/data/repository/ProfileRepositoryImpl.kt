package com.example.contexta.profile.data.repository

import com.example.contexta.data.profile.ProfileLocalRepository
import com.example.contexta.profile.data.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val client: SupabaseClient,
    private val localRepository: ProfileLocalRepository
) : ProfileRepository {

    override suspend fun getProfile(): Profile? {
        val cached = localRepository.profileFlow.first()
        if (cached != null) return cached

        val userId = client.auth.currentUserOrNull()?.id ?: return null
        val remote = client.postgrest["profiles"]
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<Profile>()

        if (remote != null) localRepository.saveProfile(remote)
        return remote
    }

    override suspend fun clearProfile() {
        localRepository.clearProfile()
    }
}
