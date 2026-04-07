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

class AuthRepository @Inject constructor(
    private val client: SupabaseClient
) {
    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signUp(name: String, email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password

            data = buildJsonObject {
                put("full_name", name)
            }
        }
    }

    suspend fun signInWithGoogle() {
        client.auth.signInWith(Google)
    }

    suspend fun signUpWithGoogle() {
        client.auth.signInWith(Google)
    }

    suspend fun logout() {
        client.auth.signOut()
    }

    fun observeSession(): Flow<SessionStatus> {
        return client.auth.sessionStatus
    }

    fun currentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }
}