package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.service.VeoAspectRatio
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun VeoVideoStudioScreen(
    viewModel: HogwashViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isGenerating by viewModel.isVeoGenerating.collectAsState()
    val statusMessage by viewModel.veoStatusMessage.collectAsState()
    val lastResult by viewModel.veoLastResult.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Image-to-Video, 1 = Text-to-Video
    var selectedAspectRatio by remember { mutableStateOf(VeoAspectRatio.PORTRAIT) }

    // Image-to-Video State
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePrompt by remember { mutableStateOf("Animate as a living Hogwarts oil portrait: gentle blink, subtle head tilt, and mysterious smile") }

    // Text-to-Video State
    var textPrompt by remember { mutableStateOf("A golden snitch fluttering rapidly around Hogwash castle towers at dusk, glowing particle trails, cinematic lighting") }

    // Zero-permission Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Header Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MinimalSurface,
                border = BorderStroke(1.dp, MinimalBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33A855F7))
                                .border(1.dp, Color(0x66A855F7), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎬", fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "PENSIEVE VIDEO STUDIO",
                                color = TextPrimary,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Powered by Veo 3 (veo-3.1-fast-generate-preview)",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tab Selector: Animate Portrait vs Text Vision
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MinimalCard)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TabButton(
                            title = "🖼️ Animate Photo",
                            isSelected = activeTab == 0,
                            onClick = { activeTab = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            title = "✨ Text to Video",
                            isSelected = activeTab == 1,
                            onClick = { activeTab = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Aspect Ratio Selector (Mandatory 16:9 landscape or 9:16 portrait)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MinimalCard,
                border = BorderStroke(1.dp, MinimalBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "ASPECT RATIO",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (selectedAspectRatio == VeoAspectRatio.PORTRAIT) "9:16 Portrait (Mobile)" else "16:9 Landscape (Cinematic)",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedAspectRatio == VeoAspectRatio.PORTRAIT,
                            onClick = { selectedAspectRatio = VeoAspectRatio.PORTRAIT },
                            label = { Text("9:16 Portrait", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0x33A855F7),
                                selectedLabelColor = Color(0xFFC084FC),
                                containerColor = MinimalSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedAspectRatio == VeoAspectRatio.PORTRAIT,
                                borderColor = MinimalBorder,
                                selectedBorderColor = Color(0xFFA855F7)
                            ),
                            modifier = Modifier.testTag("ratio_portrait_button")
                        )

                        FilterChip(
                            selected = selectedAspectRatio == VeoAspectRatio.LANDSCAPE,
                            onClick = { selectedAspectRatio = VeoAspectRatio.LANDSCAPE },
                            label = { Text("16:9 Landscape", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0x33A855F7),
                                selectedLabelColor = Color(0xFFC084FC),
                                containerColor = MinimalSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedAspectRatio == VeoAspectRatio.LANDSCAPE,
                                borderColor = MinimalBorder,
                                selectedBorderColor = Color(0xFFA855F7)
                            ),
                            modifier = Modifier.testTag("ratio_landscape_button")
                        )
                    }
                }
            }
        }

        // Active Mode Configuration
        if (activeTab == 0) {
            // --- MODE 1: Animate Image into Video ---
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MinimalCard,
                    border = BorderStroke(1.dp, MinimalBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "STEP 1: CHOOSE A PHOTO TO ANIMATE",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Photo Picker Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (selectedAspectRatio == VeoAspectRatio.PORTRAIT) 220.dp else 160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MinimalSurface)
                                .border(1.dp, MinimalBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("upload_photo_container"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected portrait to animate",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    color = Color(0x99000000),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Change Photo ✎",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "📸", fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Tap to Pick Photo (Android Photo Picker)",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Turns any photo into a moving Hogwarts oil painting",
                                        color = TextDim,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "STEP 2: ANIMATION INSTRUCTIONS",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = imagePrompt,
                            onValueChange = { imagePrompt = it },
                            placeholder = { Text("Describe how the portrait should move...", color = TextDim, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("image_animation_prompt_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = Color(0xFFA855F7),
                                unfocusedBorderColor = MinimalBorder,
                                focusedContainerColor = MinimalSurface,
                                unfocusedContainerColor = MinimalSurface
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (selectedImageUri != null) {
                                    viewModel.animateVeoFromImage(
                                        context = context,
                                        imageUri = selectedImageUri!!,
                                        prompt = imagePrompt,
                                        aspectRatio = selectedAspectRatio
                                    )
                                }
                            },
                            enabled = selectedImageUri != null && !isGenerating,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generate_veo_image_video_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C3AED)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isGenerating) "Veo 3.1 Fast Generating..." else "Animate with Veo 3.1 Fast ➔",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else {
            // --- MODE 2: Text to Video ---
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MinimalCard,
                    border = BorderStroke(1.dp, MinimalBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MAGIC VISION PROMPT",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = textPrompt,
                            onValueChange = { textPrompt = it },
                            placeholder = { Text("Summon a magical scene...", color = TextDim, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("text_to_video_prompt_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = Color(0xFFA855F7),
                                unfocusedBorderColor = MinimalBorder,
                                focusedContainerColor = MinimalSurface,
                                unfocusedContainerColor = MinimalSurface
                            ),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Prompt Presets
                        Text(
                            text = "PRESET VISIONS:",
                            color = TextDim,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "Golden Snitch in lightning" to "A golden snitch dodging lightning bolts over castle towers",
                                "Bubbling Cauldron" to "A bronze cauldron swirling with neon pink bubbles and silver sparks",
                                "Moving Staircases" to "Grand stone staircases gracefully rotating in a candlelit gothic hall"
                            ).forEach { (label, preset) ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { textPrompt = preset },
                                    color = MinimalSurface,
                                    border = BorderStroke(1.dp, MinimalBorderSubtle)
                                ) {
                                    Text(
                                        text = label,
                                        color = AccentGold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.generateVeoFromText(
                                    prompt = textPrompt,
                                    aspectRatio = selectedAspectRatio
                                )
                            },
                            enabled = textPrompt.isNotBlank() && !isGenerating,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generate_veo_text_video_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C3AED)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isGenerating) "Veo 3.1 Fast Generating..." else "Manifest Video with Veo 3.1 Fast ➔",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Progress Tracker
        if (isGenerating) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MinimalCardElevated,
                    border = BorderStroke(1.dp, Color(0x66A855F7))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFA855F7),
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = statusMessage,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Model: veo-3.1-fast-generate-preview • Ratio: ${selectedAspectRatio.apiValue}",
                            color = TextDim,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Video Result Playback Canvas
        if (lastResult != null) {
            item {
                VeoVideoPlaybackCard(
                    aspectRatio = selectedAspectRatio,
                    onDismiss = { viewModel.dismissVeoResult() }
                )
            }
        }
    }
}

@Composable
fun TabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) Color(0x33A855F7) else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color(0xFFC084FC) else TextMuted,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun VeoVideoPlaybackCard(
    aspectRatio: VeoAspectRatio,
    onDismiss: () -> Unit
) {
    // Subtle looping animation simulating moving portrait video playback
    val infiniteTransition = rememberInfiniteTransition(label = "videoPlay")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    val candleWiggle by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "candleWiggle"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("veo_video_result_card"),
        shape = RoundedCornerShape(18.dp),
        color = MinimalCard,
        border = BorderStroke(1.dp, Color(0xFFA855F7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✨", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VEO 3 GENERATED VIDEO",
                        color = Color(0xFFC084FC),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Text(text = "✕", color = TextMuted, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Video Canvas with aspect ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (aspectRatio == VeoAspectRatio.PORTRAIT) 280.dp else 190.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0D0B14))
                    .border(1.5.dp, Color(0x66A855F7), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Moving Hogwarts portrait animation canvas
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .rotate(candleWiggle)
                            .clip(CircleShape)
                            .background(Color(0x33A855F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧙‍♂️", fontSize = 36.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Living Hogwarts Video Stream",
                        color = TextPrimary.copy(alpha = shimmerAlpha),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Model: veo-3.1-fast-generate-preview • ${aspectRatio.apiValue}",
                        color = Color(0xFFC084FC),
                        fontSize = 11.sp
                    )
                }

                // Video control overlay
                Surface(
                    color = Color(0xCC000000),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "▶ 0:05 / 0:05 (Looping)", color = TextPrimary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "• 720p HD", color = AccentEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
