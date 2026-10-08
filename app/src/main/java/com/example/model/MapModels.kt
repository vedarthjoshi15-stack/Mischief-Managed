package com.example.model

enum class Zone(val label: String) {
    CASTLE("Castle Upper Floors"),
    GROUNDS("Castle Grounds"),
    DUNGEONS("Dungeon Depths"),
    FORBIDDING_WOODS("The Forbidding Foliage")
}

data class LocationNode(
    val id: String,
    val name: String,
    val parodySubtitle: String,
    val zone: Zone,
    val xPct: Float, // Normalized 0f..1f for canvas coordinate mapping
    val yPct: Float,
    val dangerLevel: String, // "Peaceful", "Mildly Perilous", "Highly Hazardous"
    val description: String,
    val isQuestTarget: Boolean = false,
    val canTriggerCreature: Boolean = false
)

data class MapEdge(
    val fromId: String,
    val toId: String,
    val weight: Int,
    val isStaircase: Boolean = false
)

data class NavigationPath(
    val nodeIds: List<String>,
    val totalDistance: Int,
    val routeDescription: String
)

data class LocationRiddle(
    val id: String,
    val locationId: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val rewardXp: Int = 30,
    val rewardPoints: Int = 15
)

data class EncounterChoice(
    val choiceText: String,
    val consequenceText: String,
    val xpDelta: Int,
    val pointsDelta: Int
)

data class CreatureEncounter(
    val id: String,
    val locationId: String,
    val creatureName: String,
    val dangerRating: String,
    val introLore: String,
    val dilemma: String,
    val choices: List<EncounterChoice>
)
