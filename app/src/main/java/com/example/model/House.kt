package com.example.model

import androidx.compose.ui.graphics.Color

enum class House(
    val displayName: String,
    val motto: String,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val mascot: String,
    val crestEmoji: String
) {
    GRYFFINDOOR(
        displayName = "Gryffindoor",
        motto = "Charge First, Read Manual Later",
        description = "Where dwell the recklessly brave, or at least those who refuse to check the hallway for angry gargoyles.",
        primaryColor = Color(0xFF7A1C1C),
        secondaryColor = Color(0xFFD4AF37),
        accentColor = Color(0xFFFFD166),
        mascot = "Sleepy Lion with Slippers",
        crestEmoji = "🦁"
    ),
    SLITHERIN(
        displayName = "Slitherin",
        motto = "Ambition, Cunning, & Wholesale Discounts",
        description = "For the resourcefully shrewd wizards who never pay full price for dragon liver and always have a contingency excuse.",
        primaryColor = Color(0xFF1B4D3E),
        secondaryColor = Color(0xFF8B9D83),
        accentColor = Color(0xFF52B788),
        mascot = "Spectacled Python",
        crestEmoji = "🐍"
    ),
    RAVENCLUE(
        displayName = "Ravenclue",
        motto = "Overthinking Trivial Riddles Since 993 A.D.",
        description = "Home to the excessively intellectual students who will spend 4 hours analyzing a door knocker instead of pulling the handle.",
        primaryColor = Color(0xFF1E3A8A),
        secondaryColor = Color(0xFFCD7F32),
        accentColor = Color(0xFF60A5FA),
        mascot = "Inquisitive Blue Raven",
        crestEmoji = "🦅"
    ),
    HUFFLEFLUFF(
        displayName = "Hufflefluff",
        motto = "Just Happy To Be Here & Brought Pastries",
        description = "The kindest, coziest folks in the castle. Zero dark wizards produced, but an unprecedented number of excellent bakers.",
        primaryColor = Color(0xFFB45309),
        secondaryColor = Color(0xFFFBBF24),
        accentColor = Color(0xFFFDE68A),
        mascot = "Cheery Chubby Badger",
        crestEmoji = "🦡"
    )
}

data class UserProfile(
    val studentName: String = "Hairy Porter-ish",
    val house: House? = null,
    val level: Int = 1,
    val xp: Int = 40,
    val housePoints: Int = 50,
    val currentTitle: String = "First-Year Wanderer",
    val completedQuests: Set<String> = emptySet(),
    val unlockedBadges: Set<String> = emptySet(),
    val solvedRiddles: Set<String> = emptySet(),
    val encountersCompleted: Int = 0,
    val isSorted: Boolean = false
) {
    val xpForNextLevel: Int
        get() = level * 100

    val xpProgress: Float
        get() = (xp % 100).toFloat() / 100f
}
