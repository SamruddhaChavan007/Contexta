package com.example.contexta.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contexta.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvents = Channel<MainNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

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
        viewModelScope.launch { authRepository.logout() }
        // NavigateToAuth fires automatically via observeSession()
    }
}
