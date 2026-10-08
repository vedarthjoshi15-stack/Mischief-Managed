package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.model.House
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun SortingHatRevealAnimation(
    house: House,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sorting_reveal")
    
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    val scaleAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleAnim),
        contentAlignment = Alignment.Center
    ) {
        // Magical Particle Sparkle Canvas Overlay
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val random = Random(42)
            for (i in 0 until 40) {
                val seedOffset = i * 37.5f
                val speed = 0.4f + (i % 6) * 0.25f
                val t = ((time * speed + seedOffset) % 1000f) / 1000f
                
                val x = (random.nextFloat() * canvasWidth)
                val y = canvasHeight - (t * canvasHeight)
                val alpha = (1f - t) * 0.85f
                val radius = 1.5f + (i % 5).toFloat()

                val particleColor = if (i % 3 == 0) house.secondaryColor else GlassGold

                drawCircle(
                    color = particleColor.copy(alpha = alpha.coerceIn(0f, 1f)),
                    radius = radius,
                    center = Offset(x, y)
                )
            }
        }

        content()
    }
}
