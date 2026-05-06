package com.example.contexta.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contexta.auth.navigation.AUTH_GRAPH
import com.example.contexta.auth.navigation.authNavGraph
import com.example.contexta.ui.transitions.LogoutTransitionOverlay

private const val ROUTE_HOME = "home_screen"

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AUTH_GRAPH) {

        authNavGraph(
            onNavigateToHome = {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(AUTH_GRAPH) { inclusive = true }
                }
            }
        )

        composable(ROUTE_HOME) { backStackEntry ->
            val viewModel: MainViewModel = hiltViewModel(backStackEntry)
            val showLogoutOverlay by viewModel.showLogoutOverlay.collectAsStateWithLifecycle()
            val isDark = isSystemInDarkTheme()

            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collect { event ->
                    if (event is MainNavigationEvent.NavigateToAuth) {
                        navController.navigate(AUTH_GRAPH) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    }
                }
            }

            Box(Modifier.fillMaxSize()) {
                MainScreen(onLogout = viewModel::logout)

                if (showLogoutOverlay) {
                    LogoutTransitionOverlay(isDark = isDark)
                }
            }
        }
    }
}