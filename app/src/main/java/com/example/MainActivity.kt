package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.House
import com.example.ui.components.BackgroundAudioPlayer
import com.example.ui.components.HogwashHeader
import com.example.ui.components.OwlPostToast
import com.example.ui.components.TubelightBottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HogwashViewModel = viewModel()
            val profile by viewModel.userProfile.collectAsState()

            HogwashTheme(house = profile.house) {
                HogwashApp(viewModel = viewModel)
            }
        }
    }
}

enum class NavigationTab(val label: String, val iconEmoji: String, val testTag: String) {
    DASHBOARD("Home", "✦", "nav_tab_dashboard"),
    MAP("Map", "◇", "nav_tab_map"),
    CLASSES("Classes", "☰", "nav_tab_classes"),
    GHOST("Guide", "○", "nav_tab_ghost"),
    VIDEO("Studio", "▶", "nav_tab_studio"),
    QUESTS("Quests", "◎", "nav_tab_quests"),
    LORE("Lore", "▤", "nav_tab_lore"),
    HAT("Sorting", "▲", "nav_tab_hat")
}

@Composable
fun HogwashApp(viewModel: HogwashViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val owlNotification by viewModel.currentOwlPost.collectAsState()
    val activeRiddle by viewModel.activeRiddle.collectAsState()
    val activeEncounter by viewModel.activeCreatureEncounter.collectAsState()
    val staircaseAlert by viewModel.staircaseMovedAlert.collectAsState()
    val isBrewingOpen by viewModel.isBrewingDialogOpen.collectAsState()

    var currentTab by remember { mutableStateOf(NavigationTab.DASHBOARD) }
    var questsInitialTab by remember { mutableStateOf(0) }

    val houseAccentColor = when (profile.house) {
        House.GRYFFINDOOR -> GryffindoorColor
        House.SLITHERIN -> SlitherinColor
        House.RAVENCLUE -> RavenclueColor
        House.HUFFLEFLUFF -> HufflefluffColor
        null -> AccentGold
    }

    // Start unsorted users on the Sorting Hat tab
    LaunchedEffect(profile.isSorted) {
        if (!profile.isSorted) {
            currentTab = NavigationTab.HAT
        }
    }

    // System BackHandler: pressing back pops to Dashboard/Home
    BackHandler(enabled = currentTab != NavigationTab.DASHBOARD) {
        currentTab = NavigationTab.DASHBOARD
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MinimalBg),
        containerColor = MinimalBg,
        topBar = {
            HogwashHeader(
                profile = profile,
                onHouseClick = { currentTab = NavigationTab.HAT },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            TubelightBottomNavBar(
                currentTab = currentTab,
                isSorted = profile.isSorted,
                houseAccentColor = houseAccentColor,
                onTabSelected = { tab ->
                    if (tab == NavigationTab.QUESTS) {
                        questsInitialTab = 0
                    }
                    currentTab = tab
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen Content
            when (currentTab) {
                NavigationTab.DASHBOARD -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToMap = { currentTab = NavigationTab.MAP },
                        onNavigateToMapWithTarget = { target ->
                            viewModel.setDestination(target)
                            currentTab = NavigationTab.MAP
                        },
                        onNavigateToClasses = { currentTab = NavigationTab.CLASSES },
                        onNavigateToGhost = { currentTab = NavigationTab.GHOST },
                        onNavigateToQuests = {
                            questsInitialTab = 0
                            currentTab = NavigationTab.QUESTS
                        },
                        onNavigateToSorting = { currentTab = NavigationTab.HAT },
                        onNavigateToEncyclopedia = { currentTab = NavigationTab.LORE },
                        onNavigateToLeaderboard = {
                            questsInitialTab = 2
                            currentTab = NavigationTab.QUESTS
                        },
                        onNavigateToVeoStudio = { currentTab = NavigationTab.VIDEO }
                    )
                }
                NavigationTab.MAP -> {
                    MapScreen(
                        viewModel = viewModel
                    )
                }
                NavigationTab.CLASSES -> {
                    TimetableScreen(
                        viewModel = viewModel,
                        onGuideMeThere = { _ ->
                            currentTab = NavigationTab.MAP
                        }
                    )
                }
                NavigationTab.GHOST -> {
                    GuideChatScreen(
                        viewModel = viewModel
                    )
                }
                NavigationTab.VIDEO -> {
                    VeoVideoStudioScreen(
                        viewModel = viewModel
                    )
                }
                NavigationTab.QUESTS -> {
                    QuestsScreen(
                        viewModel = viewModel,
                        onNavigateToMapTarget = { _ ->
                            currentTab = NavigationTab.MAP
                        },
                        onNavigateToChat = {
                            currentTab = NavigationTab.GHOST
                        },
                        onNavigateToClasses = {
                            currentTab = NavigationTab.CLASSES
                        },
                        initialTab = questsInitialTab
                    )
                }
                NavigationTab.LORE -> {
                    EncyclopediaScreen(
                        houseColor = houseAccentColor
                    )
                }
                NavigationTab.HAT -> {
                    SortingScreen(
                        viewModel = viewModel,
                        onNavigateToMap = { currentTab = NavigationTab.DASHBOARD }
                    )
                }
            }

            // Top Owl Post Notification Banner
            OwlPostToast(
                notification = owlNotification,
                onDismiss = { viewModel.dismissOwlPost() },
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // Active Interactive Brewing Dialog (from Daily Quest)
            if (isBrewingOpen) {
                BrewingDialog(
                    onBrewCompleted = { details ->
                        viewModel.completeBrewingConcoction(details)
                    },
                    onDismiss = { viewModel.dismissBrewingDialog() }
                )
            }

            // Active Location Riddle Dialog
            if (activeRiddle != null) {
                LocationRiddleDialog(
                    riddle = activeRiddle!!,
                    onSubmitAnswer = { viewModel.submitRiddleAnswer(it) },
                    onDismiss = { viewModel.dismissRiddle() }
                )
            }

            // Active Creature Encounter Dialog
            if (activeEncounter != null) {
                CreatureEncounterDialog(
                    encounter = activeEncounter!!,
                    onChooseOption = { viewModel.chooseEncounterOption(it) },
                    onDismiss = { viewModel.dismissCreatureEncounter() }
                )
            }

            // Staircase Moved Minimalist Dialog
            if (staircaseAlert != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissStaircaseAlert() },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪜", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Staircase Shifted",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    },
                    text = {
                        Text(
                            text = staircaseAlert!!,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.dismissStaircaseAlert() },
                            colors = ButtonDefaults.buttonColors(containerColor = MinimalHighlight),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Recalculate Route", color = TextPrimary, fontWeight = FontWeight.Medium)
                        }
                    },
                    containerColor = MinimalCardElevated,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}
