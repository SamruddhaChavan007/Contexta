package com.example.contexta.navigation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.contexta.navigation.Screen

data class BottomNavigationItem(
    val icon: ImageVector,
    val route: String
)

val navigationItems = listOf(
    BottomNavigationItem(
        icon = Icons.Default.GridView,
        route = Screen.Home.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.Mic,
        route = Screen.Context.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.Bolt,
        route = Screen.Execution.Route
    ),
    BottomNavigationItem(
        icon = Icons.Default.Feedback,
        route = Screen.FeedBack.Route
    )
)