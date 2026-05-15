package com.farooq.sentinelai.ui.screens.security

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.farooq.sentinelai.ui.theme.Typography

@Composable
fun SecurityScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "SECURITY PROTOCOLS", style = Typography.headlineMedium)
            Text(text = "Scanning for threats...", style = Typography.bodyMedium)
        }
    }
}
