package com.farooq.sentinelai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.farooq.sentinelai.ui.navigation.AppNavigation
import com.farooq.sentinelai.ui.theme.SentinelAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SentinelAITheme {
                AppNavigation()
            }
        }
    }
}
