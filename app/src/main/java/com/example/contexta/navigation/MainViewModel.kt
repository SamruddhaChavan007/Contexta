package com.example.contexta.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contexta.auth.data.repository.AuthRepository
import com.example.contexta.profile.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _navigationEvents = Channel<MainNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _showLogoutOverlay = MutableStateFlow(false)
    val showLogoutOverlay: StateFlow<Boolean> = _showLogoutOverlay.asStateFlow()

    init { observeSession() }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.observeSession().collect { status ->
                when (status) {
                    is SessionStatus.NotAuthenticated -> _navigationEvents.send(MainNavigationEvent.NavigateToAuth)
                    is SessionStatus.RefreshFailure -> _navigationEvents.send(MainNavigationEvent.NavigateToAuth)
                    else -> Unit
                }
            }
        }
    }

    fun logout() {
        _showLogoutOverlay.value = true
        viewModelScope.launch {
            profileRepository.clearProfile()
            authRepository.logout()
            // NavigateToAuth fires automatically via observeSession()
        }
    }
}
