package com.example.contexta.auth.data.repository

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(name: String, email: String, password: String)
    suspend fun authenticateWithGoogle()
    suspend fun logout()
    fun observeSession(): Flow<SessionStatus>
    fun currentUserId(): String?
}
