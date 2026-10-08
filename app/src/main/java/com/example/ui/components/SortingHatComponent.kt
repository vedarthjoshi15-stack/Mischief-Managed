package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.House
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SortingHatComponent(
    isAnalyzing: Boolean,
    hatMuttering: String,
    onStartSorting: (personalityInput: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var userPrompt by remember { mutableStateOf("") }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, EarthParchment.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
        color = EarthCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Title in Parchment Cream
            Text(
                text = "THE SORTING HAT",
                color = EarthParchment,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Gemini-Powered Mind Reading & House Placement",
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Hat Mascot Component
            AnimatedSortingHatMascot(
                isTalking = isAnalyzing,
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hat Muttering Bubble
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, EarthBronze.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                color = EarthBg
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAnalyzing) "🧙‍♂️ HAT IS READING YOUR MIND..." else "🧙‍♂️ HAT MUTTERS:",
                        color = EarthBronze,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (hatMuttering.isNotBlank()) "\"$hatMuttering\"" else "\"Hmm... difficult. Very difficult. Plenty of courage, I see. Not a bad mind, either. What drives your soul?\"",
                        color = EarthParchment,
                        fontFamily = FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personality Prompt Input
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                placeholder = {
                    Text(
                        text = "Tell the Hat about yourself (e.g. I value loyalty, love solving riddles, or seek power)...",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("personality_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = EarthParchment,
                    unfocusedTextColor = EarthParchment,
                    focusedBorderColor = EarthBronze,
                    unfocusedBorderColor = EarthParchment.copy(alpha = 0.3f),
                    focusedContainerColor = EarthBg,
                    unfocusedContainerColor = EarthBg
                ),
                maxLines = 3,
                enabled = !isAnalyzing
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Start Analysis Button
            Button(
                onClick = { onStartSorting(userPrompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_sorting_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EarthBronze,
                    disabledContainerColor = EarthBronzeMuted
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isAnalyzing
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = EarthParchment,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Analyzing Personality...",
                        color = EarthParchment,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        text = "Put On The Sorting Hat 🎩",
                        color = EarthBg,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedSortingHatMascot(
    isTalking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hat_mascot")

    // Bobbing / Floating animation
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Rotation / Wiggle animation
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = if (isTalking) -6f else -2f,
        targetValue = if (isTalking) 6f else 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isTalking) 300 else 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Mouth movement animation when talking
    val mouthOpen by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isTalking) 1f else 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween( if (isTalking) 250 else 1200, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mouth"
    )

    Box(
        modifier = modifier
            .offset(y = floatOffset.dp)
            .rotate(rotationAngle),
        contentAlignment = Alignment.Center
    ) {
        // Magical Aura Glow behind Hat
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EarthBronze.copy(alpha = if (isTalking) 0.5f else 0.25f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width / 1.5f
                ),
                radius = size.width / 1.5f
            )

            // Sparkle particles when analyzing
            if (isTalking) {
                val random = Random(123)
                for (i in 0..15) {
                    val angle = random.nextFloat() * 360f
                    val dist = random.nextFloat() * (size.width / 2f)
                    val px = center.x + dist * kotlin.math.cos(angle).toFloat()
                    val py = center.y + dist * kotlin.math.sin(angle).toFloat()
                    drawCircle(
                        color = EarthParchment.copy(alpha = random.nextFloat()),
                        radius = 2f + random.nextFloat() * 3f,
                        center = Offset(px, py)
                    )
                }
            }
        }

        // Vector Canvas Rendering of the Sorting Hat Mascot
        Canvas(modifier = Modifier.size(130.dp)) {
            val w = size.width
            val h = size.height

            // Hat Brim (Dark Leather / Bronze)
            val brimPath = Path().apply {
                moveTo(w * 0.05f, h * 0.82f)
                cubicTo(
                    w * 0.25f, h * 0.95f,
                    w * 0.75f, h * 0.95f,
                    w * 0.95f, h * 0.82f
                )
                cubicTo(
                    w * 0.75f, h * 0.76f,
                    w * 0.25f, h * 0.76f,
                    w * 0.05f, h * 0.82f
                )
                close()
            }
            drawPath(
                path = brimPath,
                color = Color(0xFF2A1C14)
            )
            drawPath(
                path = brimPath,
                color = EarthBronze,
                style = Stroke(width = 3f)
            )

            // Hat Cone (Buckled & Folded Wizard Hat)
            val conePath = Path().apply {
                moveTo(w * 0.18f, h * 0.80f)
                // Left crease
                cubicTo(w * 0.22f, h * 0.55f, w * 0.30f, h * 0.40f, w * 0.38f, h * 0.25f)
                // Tip bent to right
                cubicTo(w * 0.50f, h * 0.10f, w * 0.75f, h * 0.15f, w * 0.65f, h * 0.05f)
                cubicTo(w * 0.55f, h * 0.12f, w * 0.52f, h * 0.28f, w * 0.60f, h * 0.42f)
                // Right crease
                cubicTo(w * 0.70f, h * 0.58f, w * 0.78f, h * 0.70f, w * 0.82f, h * 0.80f)
                close()
            }
            drawPath(
                path = conePath,
                color = Color(0xFF38251B)
            )
            drawPath(
                path = conePath,
                color = EarthBronzeDark,
                style = Stroke(width = 3f)
            )

            // Glowing Eyes (Folds in the Leather)
            val eyeY = h * 0.48f
            // Left Eye
            drawCircle(
                color = EarthParchment,
                radius = 4f,
                center = Offset(w * 0.42f, eyeY)
            )
            // Right Eye
            drawCircle(
                color = EarthParchment,
                radius = 4f,
                center = Offset(w * 0.62f, eyeY)
            )

            // Mouth Fold (Animates open/closed when talking)
            val mouthY = h * 0.62f
            val mouthPath = Path().apply {
                moveTo(w * 0.38f, mouthY)
                cubicTo(
                    w * 0.50f, mouthY + (12f * mouthOpen),
                    w * 0.58f, mouthY + (12f * mouthOpen),
                    w * 0.66f, mouthY
                )
            }
            drawPath(
                path = mouthPath,
                color = EarthParchment,
                style = Stroke(width = 4f)
            )
        }
    }
}
