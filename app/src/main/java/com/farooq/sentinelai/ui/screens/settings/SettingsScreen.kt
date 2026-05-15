package com.farooq.sentinelai.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.farooq.sentinelai.ui.theme.Typography

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "SYSTEM CONFIGURATION", style = Typography.headlineMedium)
            Text(text = "SentinelAI v1.0.0-Alpha", style = Typography.bodyMedium)
        }
    }
}
