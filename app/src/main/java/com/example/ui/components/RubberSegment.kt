package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun RubberSegment(
    items: List<String>,
    modifier: Modifier = Modifier,
    defaultValue: String = items.firstOrNull() ?: "",
    onChange: (String, Int) -> Unit = { _, _ -> },
    trackColor: Color = Color(0xFF27272A),
    thumbColor: Color = Color(0xFFFAFAFA),
    textColor: Color = Color(0xFFFAFAFA),
    activeTextColor: Color = Color(0xFF18181B),
    radius: Dp = 10.dp,
    inset: Dp = 3.dp,
    equalSlots: Boolean = true,
    draggable: Boolean = true
) {
    val selectedIndex = remember(defaultValue, items) {
        val idx = items.indexOf(defaultValue)
        if (idx >= 0) idx else 0
    }

    var currentIndex by remember { mutableStateOf(selectedIndex) }
    var containerWidth by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    // Smooth spring animation for thumb translation
    val animProgress = remember { Animatable(currentIndex.toFloat()) }

    // Keep animProgress in sync when currentIndex changes externally
    LaunchedEffect(currentIndex) {
        animProgress.animateTo(
            targetValue = currentIndex.toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    val slotWidth = if (containerWidth > 0 && items.isNotEmpty()) {
        (containerWidth - (inset.value * 2)) / items.size
    } else {
        0f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .onSizeChanged { containerWidth = it.width.toFloat() }
            .clip(RoundedCornerShape(radius + inset))
            .background(trackColor)
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(radius + inset))
            .pointerInput(items, draggable) {
                if (!draggable || containerWidth <= 0 || items.isEmpty()) return@pointerInput
                detectHorizontalDragGestures(
                    onDragEnd = {
                        val target = animProgress.value.coerceIn(0f, (items.size - 1).toFloat()).toInt()
                        currentIndex = target
                        scope.launch {
                            animProgress.stop()
                            animProgress.animateTo(
                                target.toFloat(),
                                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                            )
                        }
                        onChange(items[target], target)
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val newPos = (animProgress.value + (dragAmount / slotWidth)).coerceIn(0f, (items.size - 1).toFloat())
                        scope.launch { animProgress.snapTo(newPos) }
                    }
                )
            }
            .pointerInput(items) {
                if (containerWidth <= 0 || items.isEmpty()) return@pointerInput
                detectTapGestures(
                    onTap = { offset ->
                        val localX = offset.x - inset.value
                        val clickedIndex = (localX / slotWidth).toInt().coerceIn(0, items.size - 1)
                        if (clickedIndex != currentIndex) {
                            currentIndex = clickedIndex
                            onChange(items[clickedIndex], clickedIndex)
                        }
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        if (slotWidth > 0f && items.isNotEmpty()) {
            val thumbOffset = inset.value + (animProgress.value * slotWidth)
            Box(
                modifier = Modifier
                    .padding(all = inset)
                    .offset(x = androidx.compose.ui.unit.Dp(thumbOffset))
                    .width(androidx.compose.ui.unit.Dp(slotWidth))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(radius))
                    .background(thumbColor)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(inset),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = if (isSelected) activeTextColor else textColor,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
