package com.example.contexta.navigation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.contexta.navigation.Screen

data class BottomNavigationItem(
    val icon: ImageVector,
    val route: String
)

val navigationItems = listOf(
    BottomNavigationItem(
        icon = Icons.Default.Event,
        route = Screen.Home.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.AddComment,
        route = Screen.Context.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.AddCircle,
        route = Screen.Execution.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.Feedback,
        route = Screen.FeedBack.Route
    )
)