package com.example.contexta.auth.navigation

sealed class AuthNavigationEvent {
    object NavigateToHome : AuthNavigationEvent()
    object NavigateToAuth : AuthNavigationEvent()
}
