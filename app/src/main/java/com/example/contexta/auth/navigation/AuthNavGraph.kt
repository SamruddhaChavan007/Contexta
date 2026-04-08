package com.example.contexta.auth.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.contexta.auth.signin.SignInScreen
import com.example.contexta.auth.signup.screen.SignUpScreen
import com.example.contexta.auth.viewmodel.AuthViewModel

internal const val AUTH_GRAPH = "auth_graph"
private const val ROUTE_SIGN_IN = "signin"
private const val ROUTE_SIGN_UP = "signup"

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavigateToHome: () -> Unit
) {
    navigation(startDestination = ROUTE_SIGN_IN, route = AUTH_GRAPH) {

        composable(ROUTE_SIGN_IN) { backStackEntry ->
            val authEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AUTH_GRAPH)
            }
            val viewModel: AuthViewModel = hiltViewModel(authEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collect { event ->
                    if (event is AuthNavigationEvent.NavigateToHome) {
                        onNavigateToHome()
                    }
                }
            }

            SignInScreen(
                uiState = uiState,
                onSignIn = { email, password -> viewModel.signin(email, password) },
                onSignInWithGoogle = viewModel::signinWithGoogle,
                onNavigateToSignUp = { navController.navigate(ROUTE_SIGN_UP) },
                onClearError = viewModel::clearError
            )
        }

        composable(ROUTE_SIGN_UP) { backStackEntry ->
            val authEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AUTH_GRAPH)
            }
            val viewModel: AuthViewModel = hiltViewModel(authEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SignUpScreen(
                uiState = uiState,
                onSignUp = { email, password, fullName ->
                    viewModel.signup(email, password, fullName)
                },
                onSignUpWithGoogle = viewModel::signupWithGoogle,
                onNavigateToSignIn = { navController.popBackStack() },
                onClearError = viewModel::clearError
            )
        }
    }
}
