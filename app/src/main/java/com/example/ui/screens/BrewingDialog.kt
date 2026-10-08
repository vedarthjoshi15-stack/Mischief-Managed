package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun BrewingDialog(
    onBrewCompleted: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1: base, 2: stirring, 3: topping, 4: result
    var selectedBase by remember { mutableStateOf("Warm Butterscotch Syrup") }
    var selectedStir by remember { mutableStateOf("3 Counter-Clockwise Swirls") }
    var selectedTopping by remember { mutableStateOf("Marshmallow Clouds & Cinnamon") }
    var isBrewing by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, glassBorderBrush(0.5f, 0.15f), RoundedCornerShape(24.dp))
                .testTag("brewing_concoction_dialog"),
            color = Color(0xEB161220) // Deep frosted glass
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GlassGold.copy(alpha = 0.2f))
                            .border(1.dp, GlassGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🍺", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ALCHEMY LAB",
                            color = GlassGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "Brew Butterbeer-ish",
                            color = TextPrimary,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Text(text = "✕", color = TextDim, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (step == 1) {
                    // Step 1: Base
                    Text(
                        text = "1. Choose Your Cauldron Base:",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val bases = listOf(
                        "Warm Butterscotch Syrup" to "Classic comforting caramel essence",
                        "Fermented Pumpkin Fizz" to "Zesty autumn sparkle with mild kick",
                        "Secret Goblin Apple Cider" to "Slightly effervescent and suspiciously green"
                    )

                    bases.forEach { (name, desc) ->
                        val isSelected = selectedBase == name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedBase = name }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassGold else GlassBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                ),
                            color = if (isSelected) GlassCardElevated else GlassCard
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = name,
                                    color = if (isSelected) GlassGold else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(text = desc, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GlassGold),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Next: Stirring Technique ➔", color = Color(0xFF0B0910), fontWeight = FontWeight.Bold)
                    }
                } else if (step == 2) {
                    // Step 2: Stirring
                    Text(
                        text = "2. Wand Stirring Direction:",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val stirs = listOf(
                        "3 Counter-Clockwise Swirls" to "Standard potion-making safety protocol",
                        "Aggressive Clockwise Whirlpool" to "May cause miniature foam tornado",
                        "Gentle Wand Figure-Eight" to "Infuses delicate vanilla aromatics"
                    )

                    stirs.forEach { (name, desc) ->
                        val isSelected = selectedStir == name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedStir = name }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassGold else GlassBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                ),
                            color = if (isSelected) GlassCardElevated else GlassCard
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = name,
                                    color = if (isSelected) GlassGold else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(text = desc, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { step = 1 },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GlassBorder)
                        ) {
                            Text("Back", color = TextSecondary)
                        }
                        Button(
                            onClick = { step = 3 },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GlassGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Next: Froth & Topping ➔", color = Color(0xFF0B0910), fontWeight = FontWeight.Bold)
                        }
                    }
                } else if (step == 3) {
                    // Step 3: Topping
                    Text(
                        text = "3. Finishing Froth & Garnish:",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val toppings = listOf(
                        "Marshmallow Clouds & Cinnamon" to "Fluffy, sweet, and melts on the tongue",
                        "Crushed Pixie Sugar Dust" to "Causes tiny sparks when sipping",
                        "Golden Salted Caramel Drizzle" to "Rich and decadently indulgent"
                    )

                    toppings.forEach { (name, desc) ->
                        val isSelected = selectedTopping == name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedTopping = name }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassGold else GlassBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                ),
                            color = if (isSelected) GlassCardElevated else GlassCard
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = name,
                                    color = if (isSelected) GlassGold else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(text = desc, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val brewDescription = "Brewed $selectedBase with $selectedStir, topped with $selectedTopping. Exquisite!"
                            onBrewCompleted(brewDescription)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("finalize_brew_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "✨ Cast Flame & Tap Tankard! ✨",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
