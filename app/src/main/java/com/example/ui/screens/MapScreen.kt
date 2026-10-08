package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.HogwashData
import com.example.model.LocationNode
import com.example.model.Zone
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun MapScreen(
    viewModel: HogwashViewModel,
    modifier: Modifier = Modifier
) {
    val currentLocId by viewModel.currentLocationId.collectAsState()
    val targetLocId by viewModel.targetLocationId.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val selectedNode by viewModel.selectedLocationNode.collectAsState()
    val staircaseAlert by viewModel.staircaseMovedAlert.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    // Pulsing animation for glowing footprints
    val infiniteTransition = rememberInfiniteTransition(label = "footprints")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GlassBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Map Control Top Bar (Frosted Glass)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GlassSurface,
                border = BorderStroke(1.dp, GlassBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MARAUDER'S MAP",
                                color = GlassGold,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "📜", fontSize = 12.sp)
                        }
                        Text(
                            text = "\"I solemnly swear that I am up to no good...\"",
                            color = TextDim,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 10.sp
                        )
                    }

                    // Staircase Moved Again Button!
                    OutlinedButton(
                        onClick = { viewModel.triggerRandomStaircaseShift() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = GlassCard
                        ),
                        border = BorderStroke(1.dp, GlassBorder),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("move_staircase_button")
                    ) {
                        Text(
                            text = "🪜 Shift Stairs!",
                            color = GlassGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Interactive Parchment Canvas inside Glass Frame with Castle Nodes & Path
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, glassBorderBrush(0.35f, 0.08f), RoundedCornerShape(20.dp))
            ) {
                // Background Parchment Image
                Image(
                    painter = painterResource(id = R.drawable.parchment_map_1791277831840),
                    contentDescription = "Marauder's Map Parchment",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.30f),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

                // SVG / Canvas Graph Connections & Footsteps
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasW = size.width
                    val canvasH = size.height

                    // 1. Draw all castle edges (subtle faint ink)
                    HogwashData.EDGES.forEach { edge ->
                        val fromNode = HogwashData.LOCATIONS.find { it.id == edge.fromId }
                        val toNode = HogwashData.LOCATIONS.find { it.id == edge.toId }
                        if (fromNode != null && toNode != null) {
                            val p1 = Offset(fromNode.xPct * canvasW, fromNode.yPct * canvasH)
                            val p2 = Offset(toNode.xPct * canvasW, toNode.yPct * canvasH)

                            val lineColor = if (edge.isStaircase) Color(0x66F59E0B) else Color(0x33FFFFFF)
                            drawLine(
                                color = lineColor,
                                start = p1,
                                end = p2,
                                strokeWidth = 2.dp.toPx(),
                                pathEffect = if (edge.isStaircase) PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f) else null
                            )
                        }
                    }

                    // 2. Draw active shortest path route (animated glowing footsteps!)
                    val pathNodes = currentPath?.nodeIds ?: emptyList()
                    if (pathNodes.size > 1) {
                        for (i in 0 until pathNodes.size - 1) {
                            val n1 = HogwashData.LOCATIONS.find { it.id == pathNodes[i] }
                            val n2 = HogwashData.LOCATIONS.find { it.id == pathNodes[i + 1] }
                            if (n1 != null && n2 != null) {
                                val p1 = Offset(n1.xPct * canvasW, n1.yPct * canvasH)
                                val p2 = Offset(n2.xPct * canvasW, n2.yPct * canvasH)

                                // Glowing trail line
                                drawLine(
                                    color = GlassGold.copy(alpha = pulseAlpha * 0.9f),
                                    start = p1,
                                    end = p2,
                                    strokeWidth = 3.5.dp.toPx()
                                )

                                // Glowing intermediate footstep dots
                                val stepCount = 5
                                for (s in 1..stepCount) {
                                    val frac = s.toFloat() / (stepCount + 1)
                                    val stepPos = Offset(
                                        p1.x + (p2.x - p1.x) * frac,
                                        p1.y + (p2.y - p1.y) * frac
                                    )
                                    drawCircle(
                                        color = GlassAmber,
                                        radius = 2.5.dp.toPx(),
                                        center = stepPos
                                    )
                                    drawCircle(
                                        color = GlassGold.copy(alpha = pulseAlpha),
                                        radius = 4.5.dp.toPx(),
                                        center = stepPos,
                                        style = Stroke(width = 1.dp.toPx())
                                    )
                                }
                            }
                        }
                    }
                }

                // Castle Location Interactive Pins
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val w = maxWidth
                    val h = maxHeight

                    HogwashData.LOCATIONS.forEach { node ->
                        val isCurrent = node.id == currentLocId
                        val isTarget = node.id == targetLocId
                        val isSelected = node.id == selectedNode?.id
                        val isInPath = currentPath?.nodeIds?.contains(node.id) == true

                        val pinX = (w * node.xPct) - 20.dp
                        val pinY = (h * node.yPct) - 20.dp

                        Box(
                            modifier = Modifier
                                .offset(x = pinX, y = pinY)
                                .size(40.dp)
                                .clickable { viewModel.selectLocationNode(node) }
                                .testTag("map_node_${node.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            // Pulsing halo for target or current
                            if (isCurrent || isTarget) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCurrent) GlassAmber.copy(alpha = pulseAlpha * 0.4f)
                                            else GlassEmerald.copy(alpha = pulseAlpha * 0.4f)
                                        )
                                )
                            }

                            // Pin core with Frosted Glass styling
                            val pinBg = when {
                                isCurrent -> GlassGold
                                isTarget -> GlassEmerald
                                isSelected -> Color(0xFF6366F1)
                                isInPath -> GlassGold.copy(alpha = 0.85f)
                                node.zone == Zone.FORBIDDING_WOODS -> Color(0xFF047857)
                                node.zone == Zone.DUNGEONS -> Color(0xFF475569)
                                else -> GlassCardElevated
                            }

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(pinBg)
                                    .border(
                                        1.5.dp,
                                        if (isSelected) Color.White else GlassBorder,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when {
                                        isCurrent -> "👣"
                                        node.isQuestTarget -> "⭐"
                                        node.canTriggerCreature -> "🐾"
                                        node.id.contains("potions") -> "🧪"
                                        node.id.contains("library") -> "📚"
                                        node.id.contains("staircase") -> "🪜"
                                        node.id.contains("tower") -> "🔭"
                                        node.id.contains("haggard") -> "🛖"
                                        node.zone == Zone.FORBIDDING_WOODS -> "🌲"
                                        else -> "🏰"
                                    },
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Route Frosted Glass HUD & Destination Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                color = GlassSurface,
                border = BorderStroke(1.dp, glassBorderBrush(0.4f, 0.1f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val targetNode = HogwashData.LOCATIONS.find { it.id == targetLocId }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GlassGold.copy(alpha = 0.2f))
                                .border(1.dp, GlassGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎯", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TARGET: ${targetNode?.name ?: "Select a Room"}",
                                color = TextPrimary,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = currentPath?.routeDescription ?: "Tap any room pin on the map to plot a route.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }

                        if (targetLocId != null && targetLocId != currentLocId) {
                            Button(
                                onClick = { viewModel.arriveAtCurrentDestination() },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassGold),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("arrive_button")
                            ) {
                                Text(
                                    text = "Walk ➔",
                                    color = Color(0xFF0B0910),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Location Detail Overlay Sheet (Frosted Glass)
        if (selectedNode != null) {
            val node = selectedNode!!
            val isCurrent = node.id == currentLocId
            val isTarget = node.id == targetLocId

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, glassBorderBrush(0.5f, 0.15f), RoundedCornerShape(24.dp)),
                color = Color(0xEB161220) // Deep Frosted Glass
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = node.name,
                                color = GlassGold,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = node.parodySubtitle,
                                color = TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(onClick = { viewModel.selectLocationNode(null) }) {
                            Text(text = "✕", color = TextDim, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = node.description,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Plot Route Button
                        Button(
                            onClick = {
                                viewModel.setDestination(node.id)
                                viewModel.selectLocationNode(null)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GlassGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("plot_route_button")
                        ) {
                            Text(
                                text = if (isTarget) "Target Set ✓" else "Plot Route 👣",
                                color = Color(0xFF0B0910),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Riddle or Creature Encounter Action
                        val hasRiddle = HogwashData.RIDDLES.any { it.locationId == node.id }
                        if (hasRiddle) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.openRiddleForLocation(node.id)
                                    viewModel.selectLocationNode(null)
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GlassBorder),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = GlassCard),
                                modifier = Modifier.testTag("solve_riddle_button")
                            ) {
                                Text(
                                    text = "Riddle ✨",
                                    color = GlassGold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (node.canTriggerCreature) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.triggerCreatureEncounter(node.id)
                                    viewModel.selectLocationNode(null)
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0x6610B981)),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0x3310B981)),
                                modifier = Modifier.testTag("encounter_creature_button")
                            ) {
                                Text(
                                    text = "Beast 🐾",
                                    color = GlassEmerald,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
