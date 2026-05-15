package com.farooq.sentinelai.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.farooq.sentinelai.ui.theme.CyanAccent
import com.farooq.sentinelai.ui.theme.NeonGreen

@Composable
fun CyberRadar(
    modifier: Modifier = Modifier,
    color: Color = CyanAccent
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Outer rings
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius * 0.9f,
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = color.copy(alpha = 0.1f),
            radius = radius * 0.6f,
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = color.copy(alpha = 0.05f),
            radius = radius * 0.3f,
            style = Stroke(width = 1.dp.toPx())
        )

        // Crosshairs
        drawLine(
            color = color.copy(alpha = 0.1f),
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = color.copy(alpha = 0.1f),
            start = Offset(center.x, center.y - radius),
            end = Offset(center.x, center.y + radius),
            strokeWidth = 1.dp.toPx()
        )

        // Rotating scanner sweep
        rotate(rotation, center) {
            drawArc(
                brush = Brush.sweepGradient(
                    0f to color.copy(alpha = 0.5f),
                    0.25f to Color.Transparent,
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = 90f,
                useCenter = true,
                size = size
            )
            
            // Scanner lead line
            drawLine(
                color = color,
                start = center,
                end = Offset(center.x, center.y - radius),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Random "Blips" (Anomalies)
        val blipAlpha = if (pulse > 1.0f) 0.8f else 0.2f
        drawCircle(
            color = NeonGreen.copy(alpha = blipAlpha),
            radius = 4.dp.toPx(),
            center = Offset(center.x + radius * 0.4f, center.y - radius * 0.3f)
        )
    }
}
