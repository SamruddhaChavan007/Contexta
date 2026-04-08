package com.example.contexta.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contexta.auth.navigation.AuthNavigationEvent
import com.example.contexta.auth.signin.SignInScreen
import com.example.contexta.auth.signup.screen.SignUpScreen
import com.example.contexta.auth.viewmodel.AuthViewModel

private const val ROUTE_SIGN_IN = "signin"
private const val ROUTE_SIGN_UP = "signup"

@Composable
fun AuthScreen(onNavigateToHome: () -> Unit) {
    val innerNavController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            if (event is AuthNavigationEvent.NavigateToHome) {
                onNavigateToHome()
            }
        }
    }

    NavHost(navController = innerNavController, startDestination = ROUTE_SIGN_IN) {
        composable(ROUTE_SIGN_IN) {
            SignInScreen(
                uiState = uiState,
                onSignIn = { email, password -> viewModel.signin(email, password) },
                onSignInWithGoogle = viewModel::signinWithGoogle,
                onNavigateToSignUp = { innerNavController.navigate(ROUTE_SIGN_UP) },
                onClearError = viewModel::clearError
            )
        }
        composable(ROUTE_SIGN_UP) {
            SignUpScreen(
                uiState = uiState,
                onSignUp = { email, password, fullName -> viewModel.signup(email, password, fullName) },
                onSignUpWithGoogle = viewModel::signupWithGoogle,
                onNavigateToSignIn = { innerNavController.popBackStack() },
                onClearError = viewModel::clearError
            )
        }
    }
}
