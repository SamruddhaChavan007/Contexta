package com.example.contexta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contexta.auth.navigation.AUTH_GRAPH
import com.example.contexta.auth.navigation.authNavGraph

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

            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collect { event ->
                    if (event is MainNavigationEvent.NavigateToAuth) {
                        navController.navigate(AUTH_GRAPH) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    }
                }
            }

            MainScreen()
        }
    }
}