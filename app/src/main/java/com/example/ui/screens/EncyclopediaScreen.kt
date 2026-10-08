package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.EncyclopediaEntry
import com.example.model.EntryCategory
import com.example.ui.theme.*

@Composable
fun EncyclopediaScreen(
    houseColor: Color = AccentGold,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<EntryCategory?>(null) }

    val filteredEntries = HogwashData.ENCYCLOPEDIA_ENTRIES.filter { entry ->
        val matchesCategory = selectedCategory == null || entry.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                entry.title.contains(searchQuery, ignoreCase = true) ||
                entry.originalParodyRef.contains(searchQuery, ignoreCase = true) ||
                entry.humorousExplanation.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "HOGWASH ENCYCLOPEDIA",
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Compendium of Questionable Magic & Hazardous Beasts",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = MinimalCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MinimalBorderSubtle)
                ) {
                    Text(
                        text = "${filteredEntries.size} / ${HogwashData.ENCYCLOPEDIA_ENTRIES.size}",
                        color = houseColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar in Minimalist Glass Card
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search spells, creatures, relics...", color = TextDim, fontSize = 13.sp) },
                leadingIcon = {
                    Text(text = "🔍", fontSize = 14.sp)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Text(text = "✕", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("encyclopedia_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = houseColor,
                    unfocusedBorderColor = MinimalBorder,
                    focusedContainerColor = MinimalCard,
                    unfocusedContainerColor = MinimalSurface
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Pills (Minimalist)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // "All" chip
                val isAllSelected = selectedCategory == null
                FilterChip(
                    selected = isAllSelected,
                    onClick = { selectedCategory = null },
                    label = { Text("All (${HogwashData.ENCYCLOPEDIA_ENTRIES.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = houseColor.copy(alpha = 0.2f),
                        selectedLabelColor = houseColor,
                        containerColor = MinimalCard,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isAllSelected,
                        borderColor = MinimalBorderSubtle,
                        selectedBorderColor = houseColor
                    )
                )

                EntryCategory.values().forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.label, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = houseColor.copy(alpha = 0.2f),
                            selectedLabelColor = houseColor,
                            containerColor = MinimalCard,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = MinimalBorderSubtle,
                            selectedBorderColor = houseColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredEntries.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MinimalCard,
                    border = BorderStroke(1.dp, MinimalBorderSubtle)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📜", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No entries matching \"$searchQuery\"",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Perhaps Peeves hid the scroll in the dungeons?",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(filteredEntries, key = { it.id }) { entry ->
                MinimalEncyclopediaCard(entry = entry, houseColor = houseColor)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MinimalEncyclopediaCard(
    entry: EncyclopediaEntry,
    houseColor: Color = AccentGold
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val categoryColor = when (entry.category) {
        EntryCategory.SPELLS -> AccentCyan
        EntryCategory.CREATURES -> AccentEmerald
        EntryCategory.OBJECTS -> AccentGold
        EntryCategory.TRADITIONS -> AccentRose
    }

    val borderColor by animateColorAsState(
        targetValue = if (isPressed) houseColor.copy(alpha = 0.8f) else MinimalBorder,
        label = "entryBorder"
    )

    val surfaceColor by animateColorAsState(
        targetValue = if (isPressed) MinimalCardElevated else MinimalCard,
        label = "entrySurface"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = { /* Expand / interact */ }
            )
            .testTag("encyclopedia_entry_card_${entry.id}"),
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = categoryColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = entry.category.label.uppercase(),
                        color = categoryColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Ref: ${entry.originalParodyRef}",
                    color = TextDim,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.weight(1f))

                // Danger level badge
                val dangerColor = when {
                    entry.dangerLevel.contains("Extreme", ignoreCase = true) || entry.dangerLevel.contains("Fatal", ignoreCase = true) -> Color(0xFFEF4444)
                    entry.dangerLevel.contains("High", ignoreCase = true) || entry.dangerLevel.contains("Severe", ignoreCase = true) -> Color(0xFFF97316)
                    entry.dangerLevel.contains("Moderate", ignoreCase = true) -> Color(0xFFEAB308)
                    else -> Color(0xFF10B981)
                }

                Surface(
                    color = dangerColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, dangerColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = entry.dangerLevel,
                        color = dangerColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.title,
                color = if (isPressed) houseColor else TextPrimary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = entry.pronunciationOrType,
                color = TextMuted,
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Effect / Info: ${entry.effectOrDiet}",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = entry.humorousExplanation,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MinimalSurface,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MinimalBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tip: ${entry.snarkyTip}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
