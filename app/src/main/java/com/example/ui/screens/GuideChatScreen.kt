package com.example.ui.screens



import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChatMessage
import com.example.service.ChatbotRole
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun GuideChatScreen(
    viewModel: HogwashViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isGhostThinking.collectAsState()
    val currentRole by viewModel.currentChatbotRole.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val suggestedQuestions = when (currentRole) {
        ChatbotRole.GENERAL_GUIDE -> listOf(
            "Where is the Potions classroom?",
            "What is Ravenclue known for?",
            "Tell me about Aragog's Cousin Greg",
            "Why do the staircases move out of spite?",
            "Why did the Headless Hunt reject you?"
        )
        ChatbotRole.COMPLEX_SCHOLAR -> listOf(
            "Analyze the thermodynamic risks of Draught of Peace",
            "Solve the alchemical ratio for silver cauldron reflux",
            "Why does Gryffindoor lose so many house points?",
            "Explain Transfiguration molecular theory"
        )
        ChatbotRole.FAST_PRANKSTER -> listOf(
            "Quick: Where is the secret shortcut?",
            "Roast Professor Snapple right now!",
            "How do I prank the prefect?",
            "Fast escape from the library!"
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBg)
    ) {
        // Chatbot Header Card (Frosted Glass)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MinimalSurface,
            border = BorderStroke(1.dp, MinimalBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, AccentCyan.copy(alpha = 0.8f), CircleShape)
                            .background(Color(0x330284C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentRole == ChatbotRole.GENERAL_GUIDE) {
                            Image(
                                painter = painterResource(id = R.drawable.nick_ghost_1791277817079),
                                contentDescription = "Ghost Guide",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Text(text = currentRole.emoji, fontSize = 24.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentRole.title,
                                color = TextPrimary,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = currentRole.emoji, fontSize = 12.sp)
                        }

                        Text(
                            text = currentRole.description,
                            color = TextMuted,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    IconButton(
                        onClick = { viewModel.clearChatHistory() },
                        modifier = Modifier.testTag("chat_clear_button")
                    ) {
                        Text(text = "🔄", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Role / Model Selector Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChatbotRole.values().forEach { role ->
                        val isSelected = currentRole == role
                        val activeColor = when (role) {
                            ChatbotRole.GENERAL_GUIDE -> AccentCyan
                            ChatbotRole.COMPLEX_SCHOLAR -> AccentEmerald
                            ChatbotRole.FAST_PRANKSTER -> AccentGold
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setChatbotRole(role) },
                            label = {
                                Text(
                                    text = "${role.emoji} ${role.roleName} (${role.modelName.substringAfter("gemini-")})",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = activeColor.copy(alpha = 0.2f),
                                selectedLabelColor = activeColor,
                                containerColor = MinimalCard,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MinimalBorderSubtle,
                                selectedBorderColor = activeColor
                            ),
                            modifier = Modifier.testTag("role_chip_${role.name}")
                        )
                    }
                }
            }
        }

        // Suggested Prompt Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            suggestedQuestions.forEach { prompt ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { viewModel.sendGhostQuestion(prompt) },
                    color = MinimalCard,
                    border = BorderStroke(1.dp, MinimalBorderSubtle)
                ) {
                    Text(
                        text = prompt,
                        color = AccentGold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Multi-Turn Chat Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MinimalChatBubble(message = msg)
            }

            if (isThinking) {
                item {
                    ChatTypingIndicator(role = currentRole)
                }
            }
        }

        val context = LocalContext.current
        val isTranscribing by viewModel.isTranscribing.collectAsState()
        val lastTranscription by viewModel.lastTranscription.collectAsState()
        var isRecording by remember { mutableStateOf(false) }
        var mediaRecorder by remember { mutableStateOf<android.media.MediaRecorder?>(null) }
        var audioFile by remember { mutableStateOf<java.io.File?>(null) }

        if (isTranscribing || lastTranscription.isNotBlank()) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                color = MinimalCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎙️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTranscribing) "Transcribing with gemini-3.5-transcribe..." else "Transcription Result:",
                            color = AccentCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lastTranscription.ifBlank { "Listening for incantation..." },
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Message Input Row
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MinimalSurface,
            border = BorderStroke(1.dp, MinimalBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Consult ${currentRole.roleName}...",
                            color = TextDim,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("guide_chat_input"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = MinimalBorder,
                        focusedContainerColor = MinimalCard,
                        unfocusedContainerColor = MinimalCard
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Microphone / Voice Incantation Button (gemini-3.5-transcribe)
                IconButton(
                    onClick = {
                        if (!isRecording) {
                            try {
                                val file = java.io.File.createTempFile("incantation_", ".mp4", context.cacheDir)
                                audioFile = file
                                val recorder = android.media.MediaRecorder().apply {
                                    setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                                    setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                                    setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AAC)
                                    setOutputFile(file.absolutePath)
                                    prepare()
                                    start()
                                }
                                mediaRecorder = recorder
                                isRecording = true
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            try {
                                mediaRecorder?.stop()
                                mediaRecorder?.release()
                                mediaRecorder = null
                                isRecording = false
                                audioFile?.let { f ->
                                    if (f.exists()) {
                                        val bytes = f.readBytes()
                                        viewModel.transcribeIncantation(bytes)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                isRecording = false
                            }
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) Color(0xFFEF4444) else MinimalCardElevated)
                        .border(1.dp, if (isRecording) Color(0xFFF87171) else AccentGold, CircleShape)
                        .testTag("guide_chat_mic_button"),
                    enabled = !isThinking
                ) {
                    Text(
                        text = if (isRecording) "⏹️" else "🎙️",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val q = inputText
                            inputText = ""
                            viewModel.sendGhostQuestion(q)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AccentCyan)
                        .testTag("guide_chat_send_button"),
                    enabled = inputText.isNotBlank() && !isThinking
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send inquiry",
                        tint = Color(0xFF09090B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MinimalChatBubble(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MinimalCardElevated)
                    .border(1.dp, MinimalBorderSubtle, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = message.roleEmoji, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            modifier = Modifier.widthIn(max = 290.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) Color(0xFF2A2338) else MinimalCard,
            border = BorderStroke(
                1.dp,
                if (isUser) Color(0xFF4C3B66) else MinimalBorderSubtle
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.senderName,
                            color = AccentCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Model tag badge
                        Surface(
                            color = MinimalSurface,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, MinimalBorderSubtle)
                        ) {
                            Text(
                                text = message.modelUsed.replace("-preview", ""),
                                color = TextDim,
                                fontSize = 8.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.text,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ChatTypingIndicator(role: ChatbotRole) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MinimalCardElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(text = role.emoji, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MinimalCard,
            border = BorderStroke(1.dp, MinimalBorderSubtle)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${role.roleName} is contemplating via ${role.modelName}...",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}