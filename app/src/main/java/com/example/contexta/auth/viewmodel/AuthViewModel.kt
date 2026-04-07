package com.example.contexta.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contexta.auth.data.repository.AuthRepository
import com.example.contexta.auth.state.AuthState
import com.example.contexta.profile.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Loading)
    val uiState: StateFlow<AuthState> = _uiState

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            authRepository.observeSession().collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        val profile = profileRepository.getProfile()
                        _uiState.value = AuthState.Authenticated(profile)
                    }

                    else -> {
                        _uiState.value = AuthState.Unauthenticated
                    }
                }
            }
        }
    }

    fun signin(email: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.signIn(email, password)
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.message ?: "SignIn failed")
            }
        }
    }

    fun signup(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            try {
                authRepository.signUp(fullName, email, password)
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.message ?: "SignUp failed")
            }
        }
    }

    fun signinWithGoogle() {
        viewModelScope.launch {
            try {
                authRepository.signInWithGoogle()
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.message ?: "Google SignIn failed")
            }
        }
    }

    fun signupWithGoogle() {
        viewModelScope.launch {
            try {
                authRepository.signUpWithGoogle()
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.message ?: "Google SignUp failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}