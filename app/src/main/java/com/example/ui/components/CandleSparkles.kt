package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.GlassAmber
import com.example.ui.theme.GlassGold
import kotlin.random.Random

data class Particle(
    val initialX: Float,
    val initialY: Float,
    val size: Float,
    val speed: Float,
    val particleColor: Color
)

@Composable
fun FloatingCandleSparkles(
    modifier: Modifier = Modifier,
    particleCount: Int = 18
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animProgress"
    )

    val particles = remember {
        val rand = Random(42)
        val palette = listOf(GlassGold, GlassAmber, Color(0xFFFF9F1C))
        List(particleCount) {
            Particle(
                initialX = rand.nextFloat(),
                initialY = rand.nextFloat(),
                size = rand.nextFloat() * 4f + 2f,
                speed = rand.nextFloat() * 0.4f + 0.6f,
                particleColor = palette[rand.nextInt(palette.size)]
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        particles.forEach { p ->
            val curY = ((p.initialY - (animProgress * p.speed)) % 1f + 1f) % 1f
            val wobbleX = kotlin.math.sin((animProgress * 6.28f * p.speed) + p.initialX * 10f) * 15f
            val curX = (p.initialX * canvasWidth + wobbleX).coerceIn(0f, canvasWidth)
            val yPx = curY * canvasHeight

            // Draw glowing halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(p.particleColor.copy(alpha = 0.7f), Color.Transparent),
                    center = Offset(curX, yPx),
                    radius = p.size * 3.5f
                ),
                radius = p.size * 3.5f,
                center = Offset(curX, yPx)
            )

            // Draw bright ember core
            drawCircle(
                color = p.particleColor,
                radius = p.size,
                center = Offset(curX, yPx)
            )
        }
    }
}
