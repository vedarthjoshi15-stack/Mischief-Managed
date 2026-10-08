package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.NavigationTab
import com.example.R
import com.example.ui.theme.*

@Composable
fun TubelightBottomNavBar(
    currentTab: NavigationTab,
    isSorted: Boolean,
    houseAccentColor: Color,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabsToDisplay = remember(isSorted) {
        if (!isSorted) {
            listOf(
                NavigationTab.HAT,
                NavigationTab.DASHBOARD,
                NavigationTab.MAP,
                NavigationTab.CLASSES,
                NavigationTab.GHOST,
                NavigationTab.VIDEO,
                NavigationTab.QUESTS,
                NavigationTab.LORE
            )
        } else {
            listOf(
                NavigationTab.DASHBOARD,
                NavigationTab.MAP,
                NavigationTab.CLASSES,
                NavigationTab.GHOST,
                NavigationTab.VIDEO,
                NavigationTab.QUESTS,
                NavigationTab.LORE
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .border(1.dp, glassBorderBrush(0.5f, 0.2f), RoundedCornerShape(32.dp)),
            color = Color(0xDD180205),
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabsToDisplay.forEach { tab ->
                    val isSelected = currentTab == tab
                    val activeColor = if (houseAccentColor != Color.Unspecified) houseAccentColor else GlassGold

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onTabSelected(tab) }
                            .background(if (isSelected) activeColor.copy(alpha = 0.15f) else Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag(tab.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-6).dp)
                                    .width(18.dp)
                                    .height(3.dp)
                                    .background(activeColor, RoundedCornerShape(2.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            Brush.radialGradient(
                                                listOf(activeColor.copy(alpha = 0.7f), Color.Transparent)
                                            )
                                        )
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (tab == NavigationTab.MAP) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_map_vector),
                                    contentDescription = "Map",
                                    tint = if (isSelected) activeColor else TextDim,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = tab.iconEmoji,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tab.label,
                                color = if (isSelected) activeColor else TextDim,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
