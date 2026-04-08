package com.example.contexta.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.contexta.auth.AuthScreen

internal const val AUTH_GRAPH = "auth_graph"
private const val ROUTE_AUTH_HOST = "auth_host"

fun NavGraphBuilder.authNavGraph(onNavigateToHome: () -> Unit) {
    navigation(startDestination = ROUTE_AUTH_HOST, route = AUTH_GRAPH) {
        composable(ROUTE_AUTH_HOST) {
            AuthScreen(onNavigateToHome = onNavigateToHome)
        }
    }
}
