package com.farooq.sentinelai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.farooq.sentinelai.ui.theme.CardBackground
import com.farooq.sentinelai.ui.theme.GlassWhite

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = GlassWhite,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(CardBackground)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    listOf(borderColor.copy(alpha = 0.3f), borderColor.copy(alpha = 0.05f))
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(borderWidth)
    ) {
        // Blur effect is computationally expensive, use with caution
        // For premium look, we use a semi-transparent background and border gradient
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
