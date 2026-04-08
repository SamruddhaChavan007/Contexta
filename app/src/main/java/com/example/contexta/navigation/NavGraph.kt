package com.example.contexta.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contexta.auth.navigation.AUTH_GRAPH
import com.example.contexta.auth.navigation.AuthNavigationEvent
import com.example.contexta.auth.navigation.authNavGraph
import com.example.contexta.auth.viewmodel.AuthViewModel

private const val ROUTE_HOME = "home"

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = AUTH_GRAPH) {

        authNavGraph(
            navController = navController,
            onNavigateToHome = {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(AUTH_GRAPH) { inclusive = true }
                }
            }
        )

        composable(ROUTE_HOME) { backStackEntry ->
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)

            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collect { event ->
                    if (event is AuthNavigationEvent.NavigateToAuth) {
                        navController.navigate(AUTH_GRAPH) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    }
                }
            }

            HomeScreen(onLogout = viewModel::logout)
        }
    }
}
@Composable
private fun HomeScreen(onLogout: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onLogout) {
            Text(text = "Logout")
        }
    }
}