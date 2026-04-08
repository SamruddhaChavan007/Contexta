package com.example.contexta.auth.state

import com.example.contexta.profile.data.model.Profile

sealed class SessionState {
    object Unknown : SessionState()
    data class Active(val profile: Profile?) : SessionState()
    object Inactive : SessionState()
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class ValidationError(val field: AuthField, val message: String) : AuthUiState()
    data class AuthError(val type: AuthErrorType, val message: String) : AuthUiState()
}

enum class AuthField {
    EMAIL, PASSWORD, FULL_NAME, GENERAL
}

enum class AuthErrorType {
    INVALID_CREDENTIALS, EMAIL_ALREADY_IN_USE, NETWORK_ERROR, UNKNOWN
}
