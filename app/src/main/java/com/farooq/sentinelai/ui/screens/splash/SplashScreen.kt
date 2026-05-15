package com.farooq.sentinelai.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farooq.sentinelai.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack),
        contentAlignment = Alignment.Center
    ) {
        GridBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = alphaAnim.value
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(CyanAccent.copy(alpha = 0.3f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                PulseRing(CyanAccent)
                
                Text(
                    text = "S",
                    style = Typography.displayLarge.copy(
                        fontSize = 60.sp,
                        color = CyanAccent,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SENTINEL AI",
                style = Typography.displayLarge.copy(
                    letterSpacing = 8.sp,
                    fontWeight = FontWeight.Black
                )
            )

            Text(
                text = "INFRASTRUCTURE MONITORING SYSTEM",
                style = Typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    color = NeonGreen
                )
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            BootSequence()
        }
    }
}

@Composable
fun GridBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "grid_anim")
    val offsetAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 50.dp.toPx()
        val strokeWidth = 1.dp.toPx()
        val color = CyanAccent.copy(alpha = 0.05f)

        var x = offsetAnim % gridSize
        while (x < size.width) {
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = strokeWidth
            )
            x += gridSize
        }

        var y = offsetAnim % gridSize
        while (y < size.height) {
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
            y += gridSize
        }
    }
}

@Composable
fun PulseRing(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_ring")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Canvas(modifier = Modifier.size(100.dp)) {
        drawCircle(
            color = color,
            radius = (size.minDimension / 2) * scale,
            alpha = alpha,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun BootSequence() {
    val bootLines = listOf(
        "INITIALIZING CORE SYSTEMS...",
        "CONNECTING TO NEURAL NETWORK...",
        "ENCRYPTING DATA STREAMS...",
        "SECURITY PROTOCOLS ACTIVE.",
        "SYSTEM READY."
    )
    var currentLineIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (currentLineIndex < bootLines.size - 1) {
            delay(500)
            currentLineIndex++
        }
    }

    Text(
        text = bootLines[currentLineIndex],
        style = Typography.bodyMedium.copy(
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.5f)
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
fun SplashPreview() {
    SentinelAITheme {
        SplashScreen(onAnimationFinished = {})
    }
}
