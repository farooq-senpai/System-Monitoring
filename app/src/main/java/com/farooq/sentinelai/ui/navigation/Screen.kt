package com.farooq.sentinelai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Splash : Screen("splash", "Splash")
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Analytics)
    object Security : Screen("security", "Security", Icons.Default.Security)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Analytics,
    Screen.Security,
    Screen.Settings
)
