package com.example.contexta.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.contexta.auth.signin.SignInScreen
import com.example.contexta.auth.signup.screen.SignUpScreen
import com.example.contexta.auth.state.AuthState
import com.example.contexta.auth.viewmodel.AuthViewModel

private const val AUTH_GRAPH = "auth_graph"
private const val ROUTE_SIGN_IN = "signin"
private const val ROUTE_SIGN_UP = "signup"
private const val ROUTE_HOME = "home"

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AUTH_GRAPH) {

        navigation(startDestination = ROUTE_SIGN_IN, route = AUTH_GRAPH) {

            composable(ROUTE_SIGN_IN) { backStackEntry ->
                val authEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AUTH_GRAPH)
                }
                val viewModel: AuthViewModel = hiltViewModel(authEntry)
                val authState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(authState) {
                    if (authState is AuthState.Authenticated) {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(AUTH_GRAPH) { inclusive = true }
                        }
                    }
                }

                SignInScreen(
                    authState = authState,
                    onSignIn = { email, password -> viewModel.signin(email, password) },
                    onSignInWithGoogle = viewModel::signinWithGoogle,
                    onNavigateToSignUp = { navController.navigate(ROUTE_SIGN_UP) }
                )
            }

            composable(ROUTE_SIGN_UP) { backStackEntry ->
                val authEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AUTH_GRAPH)
                }
                val viewModel: AuthViewModel = hiltViewModel(authEntry)
                val authState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(authState) {
                    if (authState is AuthState.Authenticated) {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(AUTH_GRAPH) { inclusive = true }
                        }
                    }
                }

                SignUpScreen(
                    authState = authState,
                    onSignUp = { email, password, fullName ->
                        viewModel.signup(email, password, fullName)
                    },
                    onSignUpWithGoogle = viewModel::signupWithGoogle,
                    onNavigateToSignIn = { navController.popBackStack() }
                )
            }
        }

        composable(ROUTE_HOME) {
            HomeScreen()
        }
    }
}

@Composable
private fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home")
    }
}