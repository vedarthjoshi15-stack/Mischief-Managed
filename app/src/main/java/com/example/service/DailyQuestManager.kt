package com.example.service

import com.example.model.*
import kotlin.random.Random

object DailyQuestManager {

    val QUEST_TEMPLATE_POOL: List<DailyQuest> = listOf(
        DailyQuest(
            id = "dq_butterbeer",
            title = "Brew a Butterbeer-ish Concoction",
            punSubtitle = "Frothy, Nutty, & Highly Unregulated",
            category = QuestCategory.BREWING,
            description = "Whip up a fizzy tankard of Butterbeer-ish with butterscotch foam, cinnamon dust, and an illegal dash of sugar-quill extract.",
            targetLocationId = "loc_hufflefluff_cellar",
            rewardXp = 50,
            rewardPoints = 25,
            targetProgress = 1,
            actionType = QuestActionType.BREW_INTERACTIVE,
            actionButtonText = "Brew Concoction 🍺"
        ),
        DailyQuest(
            id = "dq_library",
            title = "Visit the Dusty Archives",
            punSubtitle = "Whisper Softly Near Madam Pinch",
            category = QuestCategory.EXPLORATION,
            description = "Navigate through the Moving Staircases to the Library on the 2nd floor and browse ancient grimoires without dog-earing any pages.",
            targetLocationId = "loc_library",
            rewardXp = 40,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.VISIT_LOCATION,
            actionButtonText = "Plot to Library 👣"
        ),
        DailyQuest(
            id = "dq_ghost_inquiry",
            title = "Consult Nearly Headless Nick-ish",
            punSubtitle = "Mind the 45-Degree Neck Angle",
            category = QuestCategory.SOCIAL,
            description = "Ask Sir Nicholas about castle lore, the Headless Hunt's elitist rejection, or where Snapple hides his spare cauldrons.",
            rewardXp = 35,
            rewardPoints = 15,
            targetProgress = 1,
            actionType = QuestActionType.CHAT_GHOST,
            actionButtonText = "Ask Ghost 👻"
        ),
        DailyQuest(
            id = "dq_shift_staircase",
            title = "Survive a Shifting Staircase",
            punSubtitle = "Outwit 142 Flights of Spiteful Stone",
            category = QuestCategory.MISCHIEF,
            description = "Trigger or encounter a moving staircase swing and successfully recalculate your path to class.",
            targetLocationId = "loc_staircase",
            rewardXp = 45,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.SHIFT_STAIRS,
            actionButtonText = "Shift Stairs 🪜"
        ),
        DailyQuest(
            id = "dq_snapples_dungeon",
            title = "Infiltrate Snapple's Dungeon",
            punSubtitle = "Dodge 50 Unprovoked Detentions",
            category = QuestCategory.EXPLORATION,
            description = "Venture down into the chilly Dungeons of Bubbles and observe Professor Snapple sneering at boiling pewter cauldrons.",
            targetLocationId = "loc_potions",
            rewardXp = 40,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.VISIT_LOCATION,
            actionButtonText = "Enter Dungeon 🧪"
        ),
        DailyQuest(
            id = "dq_haggard_beast",
            title = "Pet a Beast at Haggard's Shack",
            punSubtitle = "Keep All 10 Wand Fingers Intact",
            category = QuestCategory.MISCHIEF,
            description = "Visit Groundskeeper Haggard's wooden cottage on the edge of the woods and encounter a Blast-Ended Screwtop or oversized pup.",
            targetLocationId = "loc_haggard_hut",
            rewardXp = 50,
            rewardPoints = 25,
            targetProgress = 1,
            actionType = QuestActionType.VISIT_LOCATION,
            actionButtonText = "Visit Shack 🛖"
        ),
        DailyQuest(
            id = "dq_solve_riddle",
            title = "Solve an Enchanted Castle Riddle",
            punSubtitle = "Wit Beyond Measure (Or Lucky Guess)",
            category = QuestCategory.ACADEMIC,
            description = "Crack an ancient riddle left at the Library, Potions dungeon, or Webby Hollow to prove your freshman intellect.",
            rewardXp = 45,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.SOLVE_RIDDLE,
            actionButtonText = "Find a Riddle ✨"
        ),
        DailyQuest(
            id = "dq_attend_lecture",
            title = "Attend a Scheduled Lecture",
            punSubtitle = "Arrive Before the Bell Tolls",
            category = QuestCategory.ACADEMIC,
            description = "Check your daily timetable and click 'Attend' on any lecture to demonstrate remarkable attendance records.",
            rewardXp = 40,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.ATTEND_CLASS,
            actionButtonText = "View Classes 📅"
        ),
        DailyQuest(
            id = "dq_hufflefluff_snack",
            title = "Sample Honey Drizzle Tart",
            punSubtitle = "Coziest Cellar in the Castle",
            category = QuestCategory.BREWING,
            description = "Visit the Hufflefluff Snack Cellar right behind the big vinegar barrel and sample fresh lemon drizzle cake.",
            targetLocationId = "loc_hufflefluff_cellar",
            rewardXp = 35,
            rewardPoints = 15,
            targetProgress = 1,
            actionType = QuestActionType.VISIT_LOCATION,
            actionButtonText = "Visit Cellar 🍯"
        ),
        DailyQuest(
            id = "dq_astronomy_tower",
            title = "Stargaze from Dizzy Tower",
            punSubtitle = "Highest Point, Draftiest Breezes",
            category = QuestCategory.EXPLORATION,
            description = "Climb the winding spiral steps to Stargazer's Dizzy Tower and map celestial constellations in the freezing night.",
            targetLocationId = "loc_astronomy_tower",
            rewardXp = 40,
            rewardPoints = 20,
            targetProgress = 1,
            actionType = QuestActionType.VISIT_LOCATION,
            actionButtonText = "Climb Tower 🔭"
        )
    )

    fun generateRandomDailyQuests(count: Int = 4, seed: Long? = null): List<DailyQuest> {
        val rand = if (seed != null) Random(seed) else Random.Default
        val shuffled = QUEST_TEMPLATE_POOL.shuffled(rand)
        return shuffled.take(count).map { it.copy() }
    }

    data class ProgressUpdateResult(
        val updatedQuests: List<DailyQuest>,
        val newlyCompleted: List<DailyQuest>,
        val totalXpEarned: Int,
        val totalPointsEarned: Int
    )

    fun advanceQuests(
        currentQuests: List<DailyQuest>,
        actionType: QuestActionType,
        targetLocationId: String? = null
    ): ProgressUpdateResult {
        val newlyCompleted = mutableListOf<DailyQuest>()
        var totalXp = 0
        var totalPoints = 0

        val updated = currentQuests.map { quest ->
            if (quest.isCompleted) {
                quest
            } else {
                val matchesAction = quest.actionType == actionType
                val matchesLocation = targetLocationId == null || quest.targetLocationId == null || quest.targetLocationId == targetLocationId

                if (matchesAction && matchesLocation) {
                    val nextProgress = quest.currentProgress + 1
                    val isNowComplete = nextProgress >= quest.targetProgress
                    if (isNowComplete) {
                        newlyCompleted.add(quest)
                        totalXp += quest.rewardXp
                        totalPoints += quest.rewardPoints
                    }
                    quest.copy(
                        currentProgress = nextProgress,
                        isCompleted = isNowComplete
                    )
                } else {
                    quest
                }
            }
        }

        return ProgressUpdateResult(
            updatedQuests = updated,
            newlyCompleted = newlyCompleted,
            totalXpEarned = totalXp,
            totalPointsEarned = totalPoints
        )
    }
}
