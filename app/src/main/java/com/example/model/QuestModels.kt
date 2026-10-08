package com.example.model

enum class QuestType {
    DAILY,
    SIDE_EXPEDITION,
    HOUSE_DUTY
}

data class QuestItem(
    val id: String,
    val title: String,
    val punSubtitle: String,
    val questType: QuestType,
    val description: String,
    val targetLocationId: String? = null,
    val rewardXp: Int,
    val rewardPoints: Int,
    val rewardBadgeId: String? = null,
    val isCompleted: Boolean = false
)

data class BadgeItem(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String,
    val rarity: String = "Common", // "Common", "Rare", "Legendary"
    val isUnlocked: Boolean = false
)

data class SortingQuestion(
    val id: Int,
    val hatRoastComment: String,
    val questionText: String,
    val options: List<SortingOption>
)

data class SortingOption(
    val text: String,
    val associatedHouse: House,
    val sarcasmReaction: String
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val isUser: Boolean,
    val timestampMs: Long = System.currentTimeMillis(),
    val ghostMood: String = "Perplexed", // "Perplexed", "Gleeful", "Melodramatic", "Indignant"
    val modelUsed: String = "gemini-3.5-flash",
    val roleEmoji: String = "👻"
) {
    val isFromUser: Boolean get() = isUser
}
