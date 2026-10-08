package com.example.model

enum class EntryCategory(val label: String, val iconName: String) {
    SPELLS("Spells & Hexes", "wand"),
    CREATURES("Magical Beasts", "dragon"),
    OBJECTS("Enchanted Items", "potion"),
    TRADITIONS("Hogwash Lore", "scroll")
}

data class EncyclopediaEntry(
    val id: String,
    val title: String,
    val originalParodyRef: String,
    val category: EntryCategory,
    val pronunciationOrType: String,
    val effectOrDiet: String,
    val humorousExplanation: String,
    val snarkyTip: String,
    val dangerLevel: String = "Harmless-ish"
)
