package com.farooq.sentinelai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.farooq.sentinelai.ui.screens.dashboard.DashboardScreen
import com.farooq.sentinelai.ui.screens.analytics.AnalyticsScreen
import com.farooq.sentinelai.ui.screens.security.SecurityScreen
import com.farooq.sentinelai.ui.screens.settings.SettingsScreen
import com.farooq.sentinelai.ui.screens.splash.SplashScreen
import com.farooq.sentinelai.ui.theme.BackgroundBlack
import com.farooq.sentinelai.ui.theme.CyanAccent
import com.farooq.sentinelai.ui.theme.SecondaryBackground
import com.farooq.sentinelai.ui.theme.Typography

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != Screen.Splash.route) {
                NavigationBar(
                    containerColor = SecondaryBackground.copy(alpha = 0.9f),
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = null) },
                            label = { Text(screen.title, style = Typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyanAccent,
                                selectedTextColor = CyanAccent,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onAnimationFinished = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen()
            }
            composable(Screen.Security.route) {
                SecurityScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
