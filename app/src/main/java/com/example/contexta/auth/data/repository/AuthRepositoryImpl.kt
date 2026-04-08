package com.example.contexta.auth.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : AuthRepository {

    override suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("full_name", name)
            }
        }
    }

    /**
     * Authenticates via Google OAuth. Supabase's OAuth flow handles both sign-in and sign-up
     * in a single call — the provider creates a new account if one does not exist.
     */
    override suspend fun authenticateWithGoogle() {
        client.auth.signInWith(Google)
    }

    override suspend fun logout() {
        client.auth.signOut()
    }

    override fun observeSession(): Flow<SessionStatus> {
        return client.auth.sessionStatus
    }

    override fun currentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }
}
