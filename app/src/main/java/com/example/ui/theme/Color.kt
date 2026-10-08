package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Minimalist Burgundy Palette
val Maroon1 = Color(0xFF3A0007)
val Maroon2 = Color(0xFF120002)
val GlassFill = Color(0x593C040B)
val GlassBorderGold = Color(0x33BAA473)
val TextCream = Color(0xFFEAE0D5)
val HeaderGold = Color(0xFFD4AF37)

// Original Minimalist Palette (Restored for compatibility)
val MinimalBg = Color(0xFF09090B)
val MinimalSurface = Color(0xFF160608) // Mapped to dark burgundy surface
val MinimalCard = Color(0x593C040B) // Mapped to GlassFill
val MinimalCardElevated = Color(0xFF22222C)
val MinimalBorder = Color(0xFF272733)
val MinimalBorderSubtle = Color(0x33BAA473) // Mapped to GlassBorderGold
val MinimalHighlight = Color(0xFF323242)

// Minimalist Accent Tokens
val AccentGold = Color(0xFFEAB308)
val AccentAmber = Color(0xFFF59E0B)
val AccentCyan = Color(0xFF06B6D4)
val AccentEmerald = Color(0xFF10B981)
val AccentRose = Color(0xFFF43F5E)

// High-Contrast Clean Typography
val TextPrimary = Color(0xFFFAFAFA)
val TextSecondary = Color(0xFFA1A1AA)
val TextMuted = Color(0xFF71717A)
val TextDim = Color(0xFF52525B)

// Minimalist House Color Tokens
val GryffindoorColor = Color(0xFFEF4444)
val SlitherinColor = Color(0xFF10B981)
val RavenclueColor = Color(0xFF3B82F6)
val HufflefluffColor = Color(0xFFF59E0B)

// Aliases for compatibility
val GlassBackground = Maroon2
val GlassSurface = MinimalSurface
val GlassCard = MinimalCard
val GlassCardElevated = Color(0xFF22222C) // Restored
val GlassBorder = MinimalBorder // Restored
val GlassBorderSubtle = MinimalBorderSubtle
val GlassHighlight = MinimalHighlight
val GlassGold = HeaderGold
val GlassAmber = AccentAmber
val GlassCyan = AccentCyan
val GlassEmerald = AccentEmerald

fun glassBorderBrush(startAlpha: Float = 0.25f, endAlpha: Float = 0.05f): Brush {
    return Brush.linearGradient(
        colors = listOf(Color.White.copy(alpha = startAlpha), Color.White.copy(alpha = endAlpha))
    )
}

// Earth Theme Palette
val EarthBg = Color(0xFF09090B)
val EarthCard = Color(0xFF18181B)
val EarthParchment = Color(0xFFDBD0BA)
val EarthBronze = Color(0xFFA67C52)
val EarthBronzeDark = Color(0xFF8D6E4C)
val EarthBronzeMuted = Color(0xFF735A3A)
val EarthParchmentLight = Color(0xFFC0A080)
