package com.example.model

enum class QuestCategory(val label: String, val iconEmoji: String) {
    BREWING("Cauldron & Alchemy", "🍺"),
    EXPLORATION("Castle Navigation", "🗺️"),
    SOCIAL("Spectral Inquiries", "👻"),
    ACADEMIC("Hogwash Studies", "📚"),
    MISCHIEF("Marauder Mayhem", "🪄")
}

enum class QuestActionType {
    BREW_INTERACTIVE,
    VISIT_LOCATION,
    CHAT_GHOST,
    SHIFT_STAIRS,
    SOLVE_RIDDLE,
    ATTEND_CLASS
}

data class DailyQuest(
    val id: String,
    val title: String,
    val punSubtitle: String,
    val category: QuestCategory,
    val description: String,
    val targetLocationId: String? = null,
    val rewardXp: Int = 40,
    val rewardPoints: Int = 20,
    val currentProgress: Int = 0,
    val targetProgress: Int = 1,
    val isCompleted: Boolean = false,
    val actionType: QuestActionType,
    val actionButtonText: String
) {
    val progressFraction: Float
        get() = if (targetProgress > 0) (currentProgress.toFloat() / targetProgress).coerceIn(0f, 1f) else 1f
}
