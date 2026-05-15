package com.farooq.sentinelai.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farooq.sentinelai.ui.theme.Typography

@Composable
fun AnalyticsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "ANALYTICS ENGINE", style = Typography.headlineMedium)
            Text(text = "Data visualization incoming...", style = Typography.bodyMedium)
        }
    }
}
