package com.example.contexta.auth.state

import com.example.contexta.profile.data.model.Profile

sealed class AuthState {
    object Loading: AuthState()
    object Unauthenticated: AuthState()
    data class Authenticated(val profile: Profile?) : AuthState()
    data class Error(val message: String) : AuthState()
}