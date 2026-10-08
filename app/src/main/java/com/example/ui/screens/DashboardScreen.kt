package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.HogwashData
import com.example.model.House
import com.example.model.QuestActionType
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun DashboardScreen(
    viewModel: HogwashViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToMapWithTarget: (String) -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToGhost: () -> Unit,
    onNavigateToQuests: () -> Unit,
    onNavigateToSorting: () -> Unit,
    onNavigateToEncyclopedia: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToVeoStudio: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val houseScores by viewModel.houseScores.collectAsState()
    val dailyQuests by viewModel.dailyQuests.collectAsState()
    val dailyStreak by viewModel.dailyStreak.collectAsState()
    val currentLocId by viewModel.currentLocationId.collectAsState()
    val targetLocId by viewModel.targetLocationId.collectAsState()

    val houseColor = when (profile.house) {
        House.GRYFFINDOOR -> GryffindoorColor
        House.SLITHERIN -> SlitherinColor
        House.RAVENCLUE -> RavenclueColor
        House.HUFFLEFLUFF -> HufflefluffColor
        null -> AccentGold
    }

    val currentLocation = HogwashData.LOCATIONS.find { it.id == currentLocId }
    val nextLecture = HogwashData.TIMETABLE.first()
    val completedQuestsCount = dailyQuests.count { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Student Identity Glass Card
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToSorting,
                modifier = Modifier.testTag("dashboard_student_card")
            ) { isPressed ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MinimalCardElevated)
                            .border(
                                1.dp,
                                if (isPressed) houseColor else MinimalBorderSubtle,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hogwash_crest_1791277800056),
                            contentDescription = "Hogwash School Crest",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.studentName,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (profile.house != null) {
                                Surface(
                                    color = if (isPressed) houseColor.copy(alpha = 0.25f) else MinimalCardElevated,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, if (isPressed) houseColor else MinimalBorderSubtle)
                                ) {
                                    Text(
                                        text = profile.house!!.displayName,
                                        color = if (isPressed) houseColor else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${profile.currentTitle} • Level ${profile.level}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${profile.housePoints} pts",
                            color = if (isPressed) houseColor else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "House Standing",
                            color = TextDim,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 2. Marauder's Map Quick Waypoint Card
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToMap,
                modifier = Modifier.testTag("dashboard_map_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CURRENT LOCATION",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Marauder's Map ➔",
                            color = if (isPressed) houseColor else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MinimalCardElevated)
                                .border(1.dp, if (isPressed) houseColor.copy(alpha = 0.5f) else MinimalBorderSubtle, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👣", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = currentLocation?.name ?: "The Castle Halls",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = currentLocation?.parodySubtitle ?: "Moving between classrooms",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToMap,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, if (isPressed) houseColor.copy(alpha = 0.6f) else MinimalBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MinimalCard
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Unfurl Map", color = TextPrimary, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.triggerRandomStaircaseShift() },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MinimalBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MinimalCard
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("🪜 Shift Stairs", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 3. Next Lecture / Class Preview Card
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToClasses,
                modifier = Modifier.testTag("dashboard_lecture_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "UPCOMING LECTURE",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = nextLecture.timeSlot,
                            color = if (isPressed) houseColor else TextDim,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = nextLecture.subjectName,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "${nextLecture.professor} • ${nextLecture.locationName}",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.setDestination(nextLecture.locationId)
                                onNavigateToMapWithTarget(nextLecture.locationId)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPressed) houseColor else MinimalCardElevated
                            ),
                            border = BorderStroke(1.dp, if (isPressed) houseColor else MinimalBorder),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Guide Me There ➔",
                                color = if (isPressed) Color(0xFF09090B) else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 4. Daily Mischief Tasks Card (Randomized Tasks)
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToQuests,
                modifier = Modifier.testTag("dashboard_quests_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAILY MISCHIEF",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔥", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "$dailyStreak Days",
                                color = if (isPressed) houseColor else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$completedQuestsCount of ${dailyQuests.size} Tasks Completed",
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "View All ➔",
                            color = if (isPressed) houseColor else TextDim,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val progressFraction = if (dailyQuests.isNotEmpty()) {
                        completedQuestsCount.toFloat() / dailyQuests.size
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (isPressed) houseColor else AccentEmerald,
                        trackColor = MinimalBorderSubtle
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick action for top uncompleted daily quest
                    val uncompleted = dailyQuests.firstOrNull { !it.isCompleted }
                    if (uncompleted != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MinimalCard,
                            border = BorderStroke(1.dp, MinimalBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uncompleted.category.iconEmoji,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uncompleted.title,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "+${uncompleted.rewardPoints} Pts",
                                    color = if (isPressed) houseColor else AccentGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Ask Ghost Quick Consult Card
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToGhost,
                modifier = Modifier.testTag("dashboard_ghost_card")
            ) { isPressed ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MinimalCardElevated)
                            .border(1.dp, if (isPressed) houseColor.copy(alpha = 0.6f) else MinimalBorderSubtle, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👻", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nearly Headless Nick-ish",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "\"Pardon my neck! Need directions or advice?\"",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    Text(
                        text = "Ask ➔",
                        color = if (isPressed) houseColor else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 6. House Cup Standings Minimalist Card
        item {
            val sortedHouses = houseScores.entries.sortedByDescending { it.value }
            val userHouseRank = if (profile.house != null) {
                sortedHouses.indexOfFirst { it.key == profile.house } + 1
            } else null

            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToLeaderboard,
                modifier = Modifier.testTag("dashboard_leaderboard_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HOUSE CUP STANDINGS",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if (userHouseRank != null) "Rank #$userHouseRank ➔" else "Standings ➔",
                            color = if (isPressed) houseColor else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sortedHouses.forEach { (house, score) ->
                            val isUserHouse = house == profile.house
                            val color = when (house) {
                                House.GRYFFINDOOR -> GryffindoorColor
                                House.SLITHERIN -> SlitherinColor
                                House.RAVENCLUE -> RavenclueColor
                                House.HUFFLEFLUFF -> HufflefluffColor
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isUserHouse) color.copy(alpha = 0.12f) else MinimalCard,
                                border = BorderStroke(
                                    1.dp,
                                    if (isUserHouse) color.copy(alpha = 0.6f) else MinimalBorderSubtle
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = house.crestEmoji,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${score}p",
                                        color = if (isUserHouse) color else TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = house.displayName.take(5),
                                        color = TextDim,
                                        fontSize = 9.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Hogwash Compendium / Encyclopedia Quick Card
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToEncyclopedia,
                modifier = Modifier.testTag("dashboard_encyclopedia_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HOGWASH ENCYCLOPEDIA",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Browse Compendium ➔",
                            color = if (isPressed) houseColor else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MinimalCardElevated)
                                .border(1.dp, if (isPressed) houseColor.copy(alpha = 0.5f) else MinimalBorderSubtle, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📖", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Compendium of Curiosities",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${HogwashData.ENCYCLOPEDIA_ENTRIES.size} Spells, Perilous Beasts & Artifacts",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("✨ Spells", "🐉 Beasts", "🧪 Relics", "📜 Lore").forEach { catLabel ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MinimalCard,
                                border = BorderStroke(1.dp, MinimalBorderSubtle)
                            ) {
                                Text(
                                    text = catLabel,
                                    color = if (isPressed) houseColor else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. Moving Portraits & Pensieve Video Studio (Veo 3 AI)
        item {
            InteractiveGlassCard(
                houseColor = houseColor,
                onClick = onNavigateToVeoStudio,
                modifier = Modifier.testTag("dashboard_veo_card")
            ) { isPressed ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MOVING PORTRAITS & PENSIEVE",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Veo 3 Studio ➔",
                            color = if (isPressed) houseColor else Color(0xFFC084FC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33A855F7))
                                .border(1.dp, if (isPressed) houseColor.copy(alpha = 0.6f) else Color(0x66A855F7), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎬", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Veo 3.1 Video Generations",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Animate photos or manifest text into 16:9 & 9:16 video",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable minimalist glassmorphic card with subtle house-specific accent
 * that gracefully activates on touch / press interaction.
 */
@Composable
fun InteractiveGlassCard(
    houseColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (isPressed: Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isPressed) houseColor.copy(alpha = 0.8f) else MinimalBorder,
        label = "border"
    )

    val surfaceColor by animateColorAsState(
        targetValue = if (isPressed) MinimalCardElevated else MinimalSurface,
        label = "surface"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        content(isPressed)
    }
}
