package com.example.contexta.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.contexta.context.ContextScreen
import com.example.contexta.execution.ExecutionScreen
import com.example.contexta.feedback.FeedBackScreen
import com.example.contexta.home.StrategyScreen
import com.example.contexta.moreoptions.screens.MoreOptionsScreen
import com.example.contexta.navigation.model.DrawerItem
import com.example.contexta.navigation.model.navigationItems
import com.example.contexta.ui.components.AppNavigationDrawer
import com.example.contexta.ui.components.BottomNavigationBar
import com.example.contexta.ui.components.CircularRevealLayout
import com.example.contexta.ui.components.TopApplicationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isOnRootTab = navigationItems.any { it.route == currentRoute }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val activity = LocalActivity.current
    var isMoreOptionsOpen by remember { mutableStateOf(false) }
    var moreVertCenter by remember { mutableStateOf(Offset.Zero) }

    BackHandler(enabled = isOnRootTab) {
        activity?.finish()
    }

    val drawerItems = listOf(
        DrawerItem(label = "Log Out", onClick = { viewModel.logout() })
    )

    AppNavigationDrawer(
        drawerState = drawerState,
        drawerItems = drawerItems
    ) {
        Box(Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopApplicationBar(
                        drawerState = drawerState,
                        onMoreOptionsClick = { isMoreOptionsOpen = true },
                        onMoreVertPositioned = { moreVertCenter = it }
                    )
                },
                bottomBar = { BottomNavigationBar(bottomNavController) }
            ) { innerPadding ->
                NavHost(
                    navController = bottomNavController,
                    startDestination = Screen.Home.Route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Home.Route) { StrategyScreen() }
                    composable(Screen.Context.Route) { ContextScreen() }
                    composable(Screen.Execution.Route) { ExecutionScreen() }
                    composable(Screen.FeedBack.Route) { FeedBackScreen() }
                }
            }

            CircularRevealLayout(
                visible = isMoreOptionsOpen,
                revealFrom = moreVertCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                MoreOptionsScreen(onClose = { isMoreOptionsOpen = false })
            }
        }
    }
}