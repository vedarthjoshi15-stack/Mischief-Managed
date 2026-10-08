package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HogwashData
import com.example.model.House
import com.example.ui.components.SortingHatComponent
import com.example.ui.components.SortingHatRevealAnimation
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun SortingScreen(
    viewModel: HogwashViewModel,
    onNavigateToMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val hatRoast by viewModel.hatRoastMessage.collectAsState()
    val isAnalyzing by viewModel.isAnalyzingPersonality.collectAsState()
    var selectedMode by remember { mutableStateOf(0) } // 0: Gemini AI Analysis, 1: Classic Questionnaire

    val currentQuestion = HogwashData.SORTING_QUESTIONS.getOrNull(currentIndex)
        ?: HogwashData.SORTING_QUESTIONS.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(EarthBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profile.isSorted && profile.house != null) {
            // Already Sorted Result Card (Glassmorphic & Animated Reveal)
            item {
                HouseSortedCard(
                    house = profile.house!!,
                    onStartExploring = onNavigateToMap,
                    onResort = { viewModel.resetSorting() }
                )
            }
        } else {
            // Sorting Mode Toggle Tabs (Earth Theme)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, EarthParchment.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                    color = EarthCard
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedMode = 0 },
                            color = if (selectedMode == 0) EarthBronze else Color.Transparent
                        ) {
                            Text(
                                text = "✨ Gemini AI Mind Reader",
                                color = if (selectedMode == 0) EarthBg else EarthParchment,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedMode = 1 },
                            color = if (selectedMode == 1) EarthBronze else Color.Transparent
                        ) {
                            Text(
                                text = "📜 Classic Questionnaire",
                                color = if (selectedMode == 1) EarthBg else EarthParchment,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (selectedMode == 0) {
                // Interactive Sorting Hat Mascot Component
                item {
                    SortingHatComponent(
                        isAnalyzing = isAnalyzing,
                        hatMuttering = hatRoast,
                        onStartSorting = { personalityInput ->
                            viewModel.analyzePersonalityWithGemini(personalityInput)
                        }
                    )
                }
            } else {
                // Classic Questionnaire Mode
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = EarthCard,
                        border = BorderStroke(1.dp, EarthParchment.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Question ${currentIndex + 1} of ${HogwashData.SORTING_QUESTIONS.size}",
                                color = EarthBronze,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentQuestion.questionText,
                                color = EarthParchment,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Options List
                itemsIndexed(currentQuestion.options) { index, option ->
                    val optionLetter = ('A' + index).toString()
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.answerSortingQuestion(index) }
                            .testTag("sorting_option_$index"),
                        color = EarthCard,
                        border = BorderStroke(1.dp, EarthParchment.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(EarthBronze.copy(alpha = 0.2f))
                                    .border(1.dp, EarthBronze, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    color = EarthParchment,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option.text,
                                color = EarthParchment,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HouseSortedCard(
    house: House,
    onStartExploring: () -> Unit,
    onResort: () -> Unit
) {
    SortingHatRevealAnimation(house = house) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, house.secondaryColor.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
            color = EarthCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚡ THE HAT HAS SPOKEN ⚡",
                    color = EarthBronze,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = house.displayName.uppercase(),
                    color = house.secondaryColor,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"${house.motto}\"",
                    color = EarthParchment,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = EarthBg,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, EarthParchment.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Mascot: ${house.mascot}",
                            color = EarthBronze,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = house.description,
                            color = EarthParchment,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onStartExploring,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_castle_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EarthBronze
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, EarthParchment.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Unfurl Marauder's Map ➔",
                        color = EarthBg,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onResort,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("resort_hat_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, EarthParchment.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Beg Hat for a Re-Sort",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
