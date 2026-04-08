package com.example.contexta.navigation

sealed class Screen(val Route: String) {
    data object Home : Screen("home_screen")
    data object Execution : Screen("execution_queue")
    data object Context : Screen("context_input")
    data object FeedBack : Screen("session_feedback")
}