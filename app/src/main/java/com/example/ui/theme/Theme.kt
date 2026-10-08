package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.House

fun getMinimalistColorScheme(house: House?): ColorScheme {
    val houseAccent = when (house) {
        House.GRYFFINDOOR -> GryffindoorColor
        House.SLITHERIN -> SlitherinColor
        House.RAVENCLUE -> RavenclueColor
        House.HUFFLEFLUFF -> HufflefluffColor
        null -> AccentGold
    }

    return darkColorScheme(
        primary = houseAccent,
        onPrimary = Color(0xFF09090B),
        primaryContainer = houseAccent.copy(alpha = 0.15f),
        onPrimaryContainer = TextPrimary,

        secondary = AccentAmber,
        onSecondary = Color(0xFF09090B),
        secondaryContainer = AccentAmber.copy(alpha = 0.12f),
        onSecondaryContainer = TextPrimary,

        tertiary = AccentCyan,
        onTertiary = Color(0xFF09090B),

        background = MinimalBg,
        onBackground = TextPrimary,

        surface = MinimalSurface,
        onSurface = TextPrimary,
        surfaceVariant = MinimalCard,
        onSurfaceVariant = TextSecondary,

        outline = MinimalBorder,
        outlineVariant = MinimalBorderSubtle
    )
}

@Composable
fun HogwashTheme(
    house: House? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = getMinimalistColorScheme(house)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
