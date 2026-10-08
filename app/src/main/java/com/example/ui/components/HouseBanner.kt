package com.example.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.UserProfile
import com.example.service.BackgroundMusicPlayer
import com.example.ui.theme.*

@Composable
fun HogwashHeader(
    profile: UserProfile,
    onHouseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val house = profile.house
    val houseColor = when (house) {
        com.example.model.House.GRYFFINDOOR -> GryffindoorColor
        com.example.model.House.SLITHERIN -> SlitherinColor
        com.example.model.House.RAVENCLUE -> RavenclueColor
        com.example.model.House.HUFFLEFLUFF -> HufflefluffColor
        null -> AccentGold
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MinimalBorder,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ),
        color = MinimalSurface,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Crest & Title Group (Minimalist)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onHouseClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MinimalCard)
                            .border(1.dp, houseColor.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hogwash_crest_1791277800056),
                            contentDescription = "Hogwash School Crest",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Hairy Porter",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(houseColor)
                            )
                        }

                        Text(
                            text = if (profile.isSorted && house != null) {
                                "${house.displayName} • Lvl ${profile.level}"
                            } else {
                                "Unsorted • Lvl ${profile.level}"
                            },
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // Points & Music Row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Points & XP Badge Pill (Minimalist)
                    Surface(
                        color = MinimalCard,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                        modifier = Modifier.testTag("house_points_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "🏆",
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.housePoints} pts",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Background Music Toggle Button
                    var musicPlaying by remember { mutableStateOf(BackgroundMusicPlayer.isMusicPlaying()) }
                    val context = LocalContext.current

                    Surface(
                        onClick = {
                            musicPlaying = BackgroundMusicPlayer.toggleMusic(context)
                        },
                        color = MinimalCard,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                        modifier = Modifier.testTag("bg_music_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = if (musicPlaying) "🎵" else "🔇",
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Minimalist XP Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile.currentTitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${profile.xp % 100} / 100 XP",
                    color = TextDim,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { profile.xpProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp)),
                color = houseColor,
                trackColor = MinimalBorderSubtle
            )
        }
    }
}
