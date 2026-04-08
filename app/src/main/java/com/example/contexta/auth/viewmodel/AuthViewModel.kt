package com.example.contexta.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contexta.auth.data.repository.AuthRepository
import com.example.contexta.auth.navigation.AuthNavigationEvent
import com.example.contexta.auth.state.AuthErrorType
import com.example.contexta.auth.state.AuthField
import com.example.contexta.auth.state.AuthUiState
import com.example.contexta.auth.state.SessionState
import com.example.contexta.auth.validation.AuthValidator
import com.example.contexta.profile.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Unknown)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _navigationEvents = Channel<AuthNavigationEvent>(Channel.BUFFERED)
    val navigationEvents: Flow<AuthNavigationEvent> = _navigationEvents.receiveAsFlow()

    // Fix #2: cancel previous job before launching a new auth operation
    private var authJob: Job? = null

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            authRepository.observeSession().collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        // Fix #3 & #8: skip profile re-fetch and duplicate NavigateToHome on token refresh
                        val wasAlreadyActive = _sessionState.value is SessionState.Active
                        val profile = if (wasAlreadyActive) {
                            (_sessionState.value as SessionState.Active).profile
                        } else {
                            profileRepository.getProfile()
                        }
                        _sessionState.value = SessionState.Active(profile)
                        if (!wasAlreadyActive) {
                            _navigationEvents.send(AuthNavigationEvent.NavigateToHome)
                        }
                    }
                    // Fix #1: only NotAuthenticated (explicit sign-out) triggers logout navigation.
                    // NetworkError, LoadingFromStorage, etc. are transient — session may still be valid.
                    is SessionStatus.NotAuthenticated -> {
                        val wasActive = _sessionState.value is SessionState.Active
                        _sessionState.value = SessionState.Inactive
                        if (wasActive) {
                            _navigationEvents.send(AuthNavigationEvent.NavigateToAuth)
                        }
                    }
                    else -> {
                        // LoadingFromStorage, NetworkError — transient states, do not change session
                    }
                }
            }
        }
    }

    fun signin(email: String, password: String) {
        authJob?.cancel()
        authJob = viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            AuthValidator.validateEmail(email)?.let {
                _uiState.value = AuthUiState.ValidationError(AuthField.EMAIL, it)
                return@launch
            }
            AuthValidator.validatePassword(password)?.let {
                _uiState.value = AuthUiState.ValidationError(AuthField.PASSWORD, it)
                return@launch
            }

            try {
                authRepository.signIn(email, password)
                // Fix #5: reset uiState after success; navigation is handled by observeAuth()
                _uiState.value = AuthUiState.Idle
            } catch (e: RestException) {
                val (type, message) = classifyRestException(e)
                _uiState.value = AuthUiState.AuthError(type, message)
            } catch (e: IOException) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.NETWORK_ERROR, "No internet connection")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.UNKNOWN, e.message ?: "Sign in failed")
            }
        }
    }

    fun signup(email: String, password: String, fullName: String) {
        authJob?.cancel()
        authJob = viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            AuthValidator.validateFullName(fullName)?.let {
                _uiState.value = AuthUiState.ValidationError(AuthField.FULL_NAME, it)
                return@launch
            }
            AuthValidator.validateEmail(email)?.let {
                _uiState.value = AuthUiState.ValidationError(AuthField.EMAIL, it)
                return@launch
            }
            AuthValidator.validatePassword(password)?.let {
                _uiState.value = AuthUiState.ValidationError(AuthField.PASSWORD, it)
                return@launch
            }

            try {
                authRepository.signUp(fullName, email, password)
                // Fix #5: reset uiState after success; navigation is handled by observeAuth()
                _uiState.value = AuthUiState.Idle
            } catch (e: RestException) {
                val (type, message) = classifyRestException(e)
                _uiState.value = AuthUiState.AuthError(type, message)
            } catch (e: IOException) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.NETWORK_ERROR, "No internet connection")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.UNKNOWN, e.message ?: "Sign up failed")
            }
        }
    }

    fun signinWithGoogle() {
        authJob?.cancel()
        authJob = viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authRepository.authenticateWithGoogle()
                _uiState.value = AuthUiState.Idle
            } catch (e: IOException) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.NETWORK_ERROR, "No internet connection")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.UNKNOWN, e.message ?: "Google sign in failed")
            }
        }
    }

    fun signupWithGoogle() {
        authJob?.cancel()
        authJob = viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authRepository.authenticateWithGoogle()
                _uiState.value = AuthUiState.Idle
            } catch (e: IOException) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.NETWORK_ERROR, "No internet connection")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.AuthError(AuthErrorType.UNKNOWN, e.message ?: "Google sign up failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Fix #4: handle logout failure and surface error to the user
            try {
                authRepository.logout()
            } catch (e: Exception) {
                _uiState.value = AuthUiState.AuthError(
                    AuthErrorType.UNKNOWN,
                    "Sign out failed. Please try again."
                )
            }
        }
    }

    fun clearError() {
        // Fix #7: only emit if not already Idle to avoid unnecessary recompositions
        if (_uiState.value !is AuthUiState.Idle) {
            _uiState.value = AuthUiState.Idle
        }
    }

    private fun classifyRestException(e: RestException): Pair<AuthErrorType, String> {
        val error = e.error.lowercase()
        return when {
            "invalid_grant" in error || "invalid_credentials" in error ->
                AuthErrorType.INVALID_CREDENTIALS to "Invalid email or password"
            "user_already_exists" in error || "already registered" in error ->
                AuthErrorType.EMAIL_ALREADY_IN_USE to "An account with this email already exists"
            else -> AuthErrorType.UNKNOWN to (e.message ?: "Something went wrong")
        }
    }
}
