package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HogwashData
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun QuestsScreen(
    viewModel: HogwashViewModel,
    onNavigateToMapTarget: (String) -> Unit,
    onNavigateToChat: () -> Unit = {},
    onNavigateToClasses: () -> Unit = {},
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val houseScores by viewModel.houseScores.collectAsState()
    val dailyQuests by viewModel.dailyQuests.collectAsState()
    val dailyStreak by viewModel.dailyStreak.collectAsState()
    val isDailyBonusClaimed by viewModel.isDailyBonusClaimed.collectAsState()

    var selectedTab by remember(initialTab) { mutableStateOf(initialTab) }

    val completedDailyCount = dailyQuests.count { it.isCompleted }
    val allDailyCompleted = dailyQuests.isNotEmpty() && dailyQuests.all { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Minimalist Frosted Tab Selector
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = GlassSurface,
                border = BorderStroke(1.dp, GlassBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GlassTabButton(
                        title = "🎯 Daily Quests",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    GlassTabButton(
                        title = "🎖️ Badges",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    GlassTabButton(
                        title = "🏆 Leaderboard",
                        isSelected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (selectedTab == 0) {
            // Daily Streak & Progress Glass Hub
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = GlassCardElevated,
                    border = BorderStroke(1.dp, glassBorderBrush(0.4f, 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Streak Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0x33F59E0B),
                                border = BorderStroke(1.dp, Color(0x66F59E0B))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "🔥", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$dailyStreak Day Streak",
                                        color = GlassGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Reroll Daily Tasks Button
                            OutlinedButton(
                                onClick = { viewModel.rerollDailyQuests() },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GlassBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = GlassCard
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("reroll_quests_button")
                            ) {
                                Text(
                                    text = "🎲 Reroll Tasks",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Daily Completion",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$completedDailyCount / ${dailyQuests.size} Done",
                                color = if (allDailyCompleted) GlassEmerald else GlassGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        val dailyProgressFraction = if (dailyQuests.isNotEmpty()) {
                            completedDailyCount.toFloat() / dailyQuests.size
                        } else 0f

                        LinearProgressIndicator(
                            progress = { dailyProgressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (allDailyCompleted) GlassEmerald else GlassGold,
                            trackColor = Color(0x33FFFFFF)
                        )

                        // Claim Bonus Cache button if all done
                        if (allDailyCompleted && !isDailyBonusClaimed) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.claimDailyBonus() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("claim_daily_bonus_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = GlassEmerald),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "🎁 Claim Daily Marauder's Cache (+100 XP, +50 Pts)",
                                    color = Color(0xFF0B0910),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Featured Side Quest (The Forbidding Foliage Expedition!)
            val sideQuest = HogwashData.QUESTS.find { it.id == "quest_forest_forager" }
            if (sideQuest != null) {
                val isCompleted = profile.completedQuests.contains(sideQuest.id)
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0x3310B981),
                        border = BorderStroke(1.dp, Color(0x6610B981))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(0x44065F46),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "SPECIAL EXPEDITION",
                                        color = Color(0xFFA7F3D0),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (isCompleted) {
                                    Text(
                                        text = "COMPLETED ✓",
                                        color = GlassEmerald,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = sideQuest.title,
                                color = GlassGold,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = sideQuest.punSubtitle,
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = sideQuest.description,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "+${sideQuest.rewardXp} XP • +${sideQuest.rewardPoints} Pts • 🌲 Forest Forager",
                                    color = GlassGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                if (!isCompleted && sideQuest.targetLocationId != null) {
                                    Button(
                                        onClick = {
                                            viewModel.setDestination(sideQuest.targetLocationId)
                                            onNavigateToMapTarget(sideQuest.targetLocationId)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GlassEmerald),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("track_sidequest_button")
                                    ) {
                                        Text(
                                            text = "Plot Route ➔",
                                            color = Color(0xFF0B0910),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Daily Quests Section Title
            item {
                Text(
                    text = "TODAY'S RANDOMIZED TASKS",
                    color = GlassGold,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // List of Randomized Daily Quests
            items(dailyQuests) { quest ->
                DailyQuestGlassCard(
                    quest = quest,
                    onActionClick = {
                        when (quest.actionType) {
                            QuestActionType.BREW_INTERACTIVE -> {
                                viewModel.openBrewingDialog()
                            }
                            QuestActionType.VISIT_LOCATION -> {
                                quest.targetLocationId?.let { target ->
                                    viewModel.setDestination(target)
                                    onNavigateToMapTarget(target)
                                }
                            }
                            QuestActionType.CHAT_GHOST -> {
                                onNavigateToChat()
                            }
                            QuestActionType.SHIFT_STAIRS -> {
                                viewModel.triggerRandomStaircaseShift()
                            }
                            QuestActionType.ATTEND_CLASS -> {
                                onNavigateToClasses()
                            }
                            QuestActionType.SOLVE_RIDDLE -> {
                                onNavigateToMapTarget(quest.targetLocationId ?: "loc_library")
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else if (selectedTab == 1) {
            // Badges Glass Grid
            items(HogwashData.BADGES) { badge ->
                val isUnlocked = profile.unlockedBadges.contains(badge.id)
                BadgeGlassCard(badge = badge, isUnlocked = isUnlocked)
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            // Minimalist Glass Leaderboard
            item {
                HouseCupGlassLeaderboard(
                    houseScores = houseScores,
                    userHouse = profile.house
                )
            }
        }
    }
}

@Composable
fun GlassTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) Color(0x40FFFFFF) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, GlassBorder) else null
    ) {
        Text(
            text = title,
            color = if (isSelected) TextPrimary else TextMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun DailyQuestGlassCard(
    quest: DailyQuest,
    onActionClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (quest.isCompleted) Color(0x2210B981) else GlassCard,
        border = BorderStroke(
            1.dp,
            if (quest.isCompleted) Color(0x4410B981) else GlassBorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Tag
                Surface(
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${quest.category.iconEmoji} ${quest.category.label}",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (quest.isCompleted) {
                    Text(
                        text = "COMPLETED ✓",
                        color = GlassEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                } else {
                    Text(
                        text = "+${quest.rewardXp} XP • +${quest.rewardPoints} Pts",
                        color = GlassGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = quest.title,
                color = if (quest.isCompleted) GlassEmerald else TextPrimary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = quest.punSubtitle,
                color = TextDim,
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = quest.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (!quest.isCompleted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlassCardElevated
                        ),
                        border = BorderStroke(1.dp, GlassBorder),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("action_quest_${quest.id}")
                    ) {
                        Text(
                            text = quest.actionButtonText,
                            color = GlassGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeGlassCard(badge: BadgeItem, isUnlocked: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isUnlocked) GlassCardElevated else Color(0x2215121E),
        border = if (isUnlocked) {
            BorderStroke(1.dp, glassBorderBrush(0.5f, 0.15f))
        } else {
            BorderStroke(1.dp, GlassBorderSubtle)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) GlassGold.copy(alpha = 0.2f) else Color(0x22FFFFFF))
                    .border(
                        1.dp,
                        if (isUnlocked) GlassGold.copy(alpha = 0.6f) else GlassBorderSubtle,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) badge.iconEmoji else "🔒",
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = badge.name,
                        color = if (isUnlocked) GlassGold else TextDim,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (isUnlocked) {
                        Text(text = "✦ Unlocked", color = GlassEmerald, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = badge.description,
                    color = if (isUnlocked) TextSecondary else TextDim,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun HouseCupGlassLeaderboard(
    houseScores: Map<House, Int>,
    userHouse: House?
) {
    val maxScore = (houseScores.values.maxOrNull() ?: 400).coerceAtLeast(1)
    val sortedHouses = houseScores.toList().sortedByDescending { it.second }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = GlassSurface,
        border = BorderStroke(1.dp, glassBorderBrush(0.4f, 0.15f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "🏆 THE ANNUAL HOUSE CUP",
                color = GlassGold,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Live Standings & House Point Tally",
                color = TextMuted,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            sortedHouses.forEachIndexed { rank, (house, score) ->
                val isUserHouse = house == userHouse
                val progress by animateFloatAsState(
                    targetValue = score.toFloat() / maxScore,
                    label = "bar"
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isUserHouse) house.primaryColor.copy(alpha = 0.25f) else GlassCard,
                    border = BorderStroke(
                        1.dp,
                        if (isUserHouse) house.accentColor.copy(alpha = 0.5f) else GlassBorderSubtle
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${rank + 1}",
                                color = GlassGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(28.dp)
                            )

                            Text(
                                text = house.displayName + if (isUserHouse) " (YOUR HOUSE)" else "",
                                color = if (isUserHouse) house.accentColor else TextPrimary,
                                fontFamily = FontFamily.Serif,
                                fontWeight = if (isUserHouse) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "$score pts",
                                color = GlassGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = house.primaryColor,
                            trackColor = Color(0x33FFFFFF)
                        )
                    }
                }
            }
        }
    }
}
