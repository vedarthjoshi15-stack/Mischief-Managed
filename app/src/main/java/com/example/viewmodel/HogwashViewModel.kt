package com.example.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HogwashData
import com.example.model.*
import com.example.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OwlPostNotification(
    val title: String,
    val message: String,
    val pointsChange: Int = 0,
    val xpChange: Int = 0,
    val iconEmoji: String = "📜",
    val id: Long = System.currentTimeMillis()
)

class HogwashViewModel : ViewModel() {

    // --- Profile & House State ---
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _houseScores = MutableStateFlow(
        mapOf(
            House.GRYFFINDOOR to 340,
            House.SLITHERIN to 355,
            House.RAVENCLUE to 310,
            House.HUFFLEFLUFF to 290
        )
    )
    val houseScores: StateFlow<Map<House, Int>> = _houseScores.asStateFlow()

    // --- Daily Quest System State ---
    private val _dailyQuests = MutableStateFlow<List<DailyQuest>>(
        DailyQuestManager.generateRandomDailyQuests()
    )
    val dailyQuests: StateFlow<List<DailyQuest>> = _dailyQuests.asStateFlow()

    private val _dailyStreak = MutableStateFlow(3)
    val dailyStreak: StateFlow<Int> = _dailyStreak.asStateFlow()

    private val _isDailyBonusClaimed = MutableStateFlow(false)
    val isDailyBonusClaimed: StateFlow<Boolean> = _isDailyBonusClaimed.asStateFlow()

    private val _isBrewingDialogOpen = MutableStateFlow(false)
    val isBrewingDialogOpen: StateFlow<Boolean> = _isBrewingDialogOpen.asStateFlow()

    // --- Sorting Hat Quiz State ---
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _quizAnswers = MutableStateFlow<Map<Int, House>>(emptyMap())
    private val _hatRoastMessage = MutableStateFlow("Ah, step forward! Sit on the stool and try not to get earwax on my brim.")
    val hatRoastMessage: StateFlow<String> = _hatRoastMessage.asStateFlow()

    private val _isSortingComplete = MutableStateFlow(false)
    val isSortingComplete: StateFlow<Boolean> = _isSortingComplete.asStateFlow()

    // --- Marauder's Map State ---
    private val _currentLocationId = MutableStateFlow("loc_great_hall")
    val currentLocationId: StateFlow<String> = _currentLocationId.asStateFlow()

    private val _targetLocationId = MutableStateFlow<String?>("loc_potions")
    val targetLocationId: StateFlow<String?> = _targetLocationId.asStateFlow()

    private val _currentPath = MutableStateFlow<NavigationPath?>(null)
    val currentPath: StateFlow<NavigationPath?> = _currentPath.asStateFlow()

    private val _selectedLocationNode = MutableStateFlow<LocationNode?>(null)
    val selectedLocationNode: StateFlow<LocationNode?> = _selectedLocationNode.asStateFlow()

    private val _staircaseMovedAlert = MutableStateFlow<String?>(null)
    val staircaseMovedAlert: StateFlow<String?> = _staircaseMovedAlert.asStateFlow()

    // --- Active Riddle / Encounter Modals ---
    private val _activeRiddle = MutableStateFlow<LocationRiddle?>(null)
    val activeRiddle: StateFlow<LocationRiddle?> = _activeRiddle.asStateFlow()

    private val _activeCreatureEncounter = MutableStateFlow<CreatureEncounter?>(null)
    val activeCreatureEncounter: StateFlow<CreatureEncounter?> = _activeCreatureEncounter.asStateFlow()

    private val _lastEncounterOutcome = MutableStateFlow<String?>(null)
    val lastEncounterOutcome: StateFlow<String?> = _lastEncounterOutcome.asStateFlow()

    // --- Timetable State ---
    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    // --- Ghost Guide Chat State ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "init_1",
                senderName = "Nearly Headless Nick-ish",
                text = "Greetings, fresh-faced scholar! Sir Nicholas at your humble spectral service! *crack* Pardon my neck, forty-five degrees as always! Ask me anything about Hogwash—locations, potions, professors, or why the stairs are misbehaving!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _currentChatbotRole = MutableStateFlow(ChatbotRole.GENERAL_GUIDE)
    val currentChatbotRole: StateFlow<ChatbotRole> = _currentChatbotRole.asStateFlow()

    private val _isGhostThinking = MutableStateFlow(false)
    val isGhostThinking: StateFlow<Boolean> = _isGhostThinking.asStateFlow()

    // --- Veo 3 Video Generation State ---
    private val _isVeoGenerating = MutableStateFlow(false)
    val isVeoGenerating: StateFlow<Boolean> = _isVeoGenerating.asStateFlow()

    private val _veoStatusMessage = MutableStateFlow("")
    val veoStatusMessage: StateFlow<String> = _veoStatusMessage.asStateFlow()

    private val _veoLastResult = MutableStateFlow<VeoGenerationResult?>(null)
    val veoLastResult: StateFlow<VeoGenerationResult?> = _veoLastResult.asStateFlow()

    // --- Owl Post Notification Banner ---
    private val _currentOwlPost = MutableStateFlow<OwlPostNotification?>(null)
    val currentOwlPost: StateFlow<OwlPostNotification?> = _currentOwlPost.asStateFlow()

    init {
        recalculatePath("loc_great_hall", "loc_potions")
    }

    // --- Daily Quest Operations ---
    fun advanceDailyQuests(actionType: QuestActionType, locationId: String? = null) {
        val result = DailyQuestManager.advanceQuests(_dailyQuests.value, actionType, locationId)
        _dailyQuests.value = result.updatedQuests

        if (result.newlyCompleted.isNotEmpty()) {
            awardXpAndPoints(
                xp = result.totalXpEarned,
                points = result.totalPointsEarned,
                reason = "Completed Daily Quest: ${result.newlyCompleted.first().title}"
            )

            result.newlyCompleted.forEach { q ->
                triggerOwlPost(
                    title = "Daily Quest Completed! 🎯",
                    message = "${q.title} (+${q.rewardXp} XP, +${q.rewardPoints} House Pts)",
                    pointsChange = q.rewardPoints,
                    xpChange = q.rewardXp,
                    iconEmoji = "🎯"
                )
            }
        }
    }

    fun rerollDailyQuests() {
        _dailyQuests.value = DailyQuestManager.generateRandomDailyQuests()
        _isDailyBonusClaimed.value = false
        triggerOwlPost(
            title = "Marauder's Daily Refresh 🎲",
            message = "New randomized tasks unrolled on your parchment!",
            iconEmoji = "🎲"
        )
    }

    fun openBrewingDialog() {
        _isBrewingDialogOpen.value = true
    }

    fun dismissBrewingDialog() {
        _isBrewingDialogOpen.value = false
    }

    fun completeBrewingConcoction(brewDetails: String) {
        _isBrewingDialogOpen.value = false
        advanceDailyQuests(QuestActionType.BREW_INTERACTIVE)
        awardXpAndPoints(50, 25, "Brewed Butterbeer-ish Concoction")
        triggerOwlPost(
            title = "Butterbeer-ish Brewed! 🍺",
            message = brewDetails,
            pointsChange = 25,
            xpChange = 50,
            iconEmoji = "🍺"
        )
    }

    fun claimDailyBonus() {
        if (!_isDailyBonusClaimed.value && _dailyQuests.value.all { it.isCompleted }) {
            _isDailyBonusClaimed.value = true
            _dailyStreak.value += 1
            awardXpAndPoints(100, 50, "Completed All Daily Quests")
            triggerOwlPost(
                title = "🎁 Daily Marauder's Cache!",
                message = "All daily quests cleared! +100 XP, +50 House Points, Streak: ${_dailyStreak.value} days!",
                pointsChange = 50,
                xpChange = 100,
                iconEmoji = "🏆"
            )
        }
    }

    // --- Navigation & Dijkstra ---
    fun setDestination(targetId: String) {
        _targetLocationId.value = targetId
        recalculatePath(_currentLocationId.value, targetId)
        val targetNode = HogwashData.LOCATIONS.find { it.id == targetId }
        triggerOwlPost(
            title = "Marauder's Companion",
            message = "Route plotted to ${targetNode?.name ?: "Destination"}! Follow the glowing footsteps.",
            iconEmoji = "👣"
        )
    }

    fun selectLocationNode(node: LocationNode?) {
        _selectedLocationNode.value = node
    }

    fun arriveAtCurrentDestination() {
        val target = _targetLocationId.value ?: return
        _currentLocationId.value = target
        val node = HogwashData.LOCATIONS.find { it.id == target }
        _currentPath.value = null

        awardXpAndPoints(xp = 20, points = 10, reason = "Arrived at ${node?.name}")

        // Advance daily quest if destination matches a daily task (e.g. Visit library, visit potions, visit haggard's, etc.)
        advanceDailyQuests(QuestActionType.VISIT_LOCATION, target)

        // Check if destination is Webby Hollow (Side Quest: Forest Forager)
        if (target == "loc_webby_hollow") {
            completeQuest("quest_forest_forager")
            unlockBadge("badge_forest_forager")
        }

        // Trigger encounter if applicable
        if (node?.canTriggerCreature == true) {
            triggerCreatureEncounter(target)
        }
    }

    private fun recalculatePath(startId: String, endId: String) {
        val path = DijkstraRouter.findShortestPath(startId, endId)
        _currentPath.value = path
    }

    fun triggerRandomStaircaseShift() {
        val start = _currentLocationId.value
        val end = _targetLocationId.value ?: "loc_astronomy_tower"
        val result = DijkstraRouter.triggerStaircaseShift(start, end)
        _currentPath.value = result.path
        _staircaseMovedAlert.value = result.moveEventText

        advanceDailyQuests(QuestActionType.SHIFT_STAIRS, "loc_staircase")

        triggerOwlPost(
            title = "STAIRCASE MOVED AGAIN!",
            message = result.moveEventText ?: "The steps swung 90 degrees out of spite! Path recalculated.",
            pointsChange = -5,
            iconEmoji = "🪜"
        )
        deductPoints(5, "Moving Staircase Inconvenience Tax")
        unlockBadge("badge_staircase_survivor")
    }

    fun dismissStaircaseAlert() {
        _staircaseMovedAlert.value = null
    }

    // --- Sorting Hat ---
    fun answerSortingQuestion(optionIndex: Int) {
        val currentQ = HogwashData.SORTING_QUESTIONS[_currentQuizIndex.value]
        val selectedOption = currentQ.options[optionIndex]

        val updatedMap = _quizAnswers.value.toMutableMap()
        updatedMap[_currentQuizIndex.value] = selectedOption.associatedHouse
        _quizAnswers.value = updatedMap

        _hatRoastMessage.value = selectedOption.sarcasmReaction

        if (_currentQuizIndex.value < HogwashData.SORTING_QUESTIONS.size - 1) {
            _currentQuizIndex.value += 1
        } else {
            finalizeSorting()
        }
    }

    private fun finalizeSorting() {
        val tallies = mutableMapOf<House, Int>()
        _quizAnswers.value.values.forEach { h ->
            tallies[h] = (tallies[h] ?: 0) + 1
        }
        val sortedHouse = tallies.maxByOrNull { it.value }?.key ?: House.GRYFFINDOOR

        _userProfile.value = _userProfile.value.copy(
            house = sortedHouse,
            isSorted = true,
            currentTitle = when (sortedHouse) {
                House.GRYFFINDOOR -> "Brave Door-Kicker"
                House.SLITHERIN -> "Bargain-Hunting Viper"
                House.RAVENCLUE -> "Overthinking Owl"
                House.HUFFLEFLUFF -> "Master Pastry Alchemist"
            }
        )
        _isSortingComplete.value = true

        unlockBadge("badge_sorting_survivor")
        awardXpAndPoints(xp = 50, points = 30, reason = "Sorted into ${sortedHouse.displayName}!")

        triggerOwlPost(
            title = "Sorting Complete!",
            message = "The Hat shouted: ${sortedHouse.displayName.uppercase()}! 30 points awarded to your house!",
            pointsChange = 30,
            xpChange = 50,
            iconEmoji = "🎩"
        )
    }

    
    // --- Gemini Sorting Hat Personality Analysis ---
    private val _isAnalyzingPersonality = MutableStateFlow(false)
    val isAnalyzingPersonality: StateFlow<Boolean> = _isAnalyzingPersonality.asStateFlow()

    fun analyzePersonalityWithGemini(userPrompt: String) {
        val input = if (userPrompt.isBlank()) "I value bravery, curiosity, loyalty, and ambition!" else userPrompt
        _isAnalyzingPersonality.value = true
        _hatRoastMessage.value = "Prying into your deepest memories and motives..."

        viewModelScope.launch {
            val analysisQuestion = "The student says about themselves: ''. Based on this, roast them gently in character as the Sorting Hat, and declare which house (GRYFFINDOOR, SLITHERIN, RAVENCLUE, or HUFFLEFLUFF) fits them best and why."
            val response = GeminiService.askGhostGuide(analysisQuestion)
            _hatRoastMessage.value = response
            _isAnalyzingPersonality.value = false

            val lower = response.lowercase()
            val chosenHouse = when {
                lower.contains("slitherin") || lower.contains("slytherin") -> House.SLITHERIN
                lower.contains("ravenclue") || lower.contains("ravenclaw") -> House.RAVENCLUE
                lower.contains("hufflefluff") || lower.contains("hufflepuff") -> House.HUFFLEFLUFF
                else -> House.GRYFFINDOOR
            }

            _userProfile.value = _userProfile.value.copy(
                house = chosenHouse,
                isSorted = true,
                currentTitle = when (chosenHouse) {
                    House.GRYFFINDOOR -> "Brave Door-Kicker"
                    House.SLITHERIN -> "Bargain-Hunting Viper"
                    House.RAVENCLUE -> "Overthinking Owl"
                    House.HUFFLEFLUFF -> "Master Pastry Alchemist"
                }
            )
            _isSortingComplete.value = true
            unlockBadge("badge_sorting_survivor")
            awardXpAndPoints(xp = 50, points = 30, reason = "Sorted into " + chosenHouse.displayName + "!")
            triggerOwlPost(
                title = "Sorting Complete!",
                message = "The Hat shouted: " + chosenHouse.displayName.uppercase() + "! 30 points awarded to your house!",
                pointsChange = 30,
                xpChange = 50,
                iconEmoji = "🎩"
            )
        }
    }

fun resetSorting() {
        _currentQuizIndex.value = 0
        _quizAnswers.value = emptyMap()
        _isSortingComplete.value = false
        _hatRoastMessage.value = "Back again? Did you complain to Headmaster Dumbledorf? Very well, let's probe that skull again..."
        _userProfile.value = _userProfile.value.copy(isSorted = false, house = null)
    }

    // --- Location Riddles ---
    fun openRiddleForLocation(locationId: String) {
        val riddle = HogwashData.RIDDLES.find { it.locationId == locationId }
        _activeRiddle.value = riddle
    }

    fun submitRiddleAnswer(optionIndex: Int) {
        val riddle = _activeRiddle.value ?: return
        if (optionIndex == riddle.correctIndex) {
            awardXpAndPoints(riddle.rewardXp, riddle.rewardPoints, "Solved ${riddle.locationId} Riddle!")
            advanceDailyQuests(QuestActionType.SOLVE_RIDDLE, riddle.locationId)

            val solved = _userProfile.value.solvedRiddles + riddle.id
            _userProfile.value = _userProfile.value.copy(solvedRiddles = solved)

            if (riddle.locationId == "loc_library") unlockBadge("badge_library_lurker")
            if (riddle.locationId == "loc_webby_hollow") {
                unlockBadge("badge_forest_forager")
                completeQuest("quest_forest_forager")
            }

            triggerOwlPost(
                title = "Riddle Solved!",
                message = riddle.explanation,
                pointsChange = riddle.rewardPoints,
                xpChange = riddle.rewardXp,
                iconEmoji = "✨"
            )
        } else {
            triggerOwlPost(
                title = "Wrong Answer!",
                message = "The knocker laughs at your blunder! Try again after consulting the archives.",
                iconEmoji = "❌"
            )
        }
        _activeRiddle.value = null
    }

    fun dismissRiddle() {
        _activeRiddle.value = null
    }

    // --- Creature Encounters ---
    fun triggerCreatureEncounter(locationId: String) {
        val encounter = HogwashData.CREATURE_ENCOUNTERS.find { it.locationId == locationId }
            ?: HogwashData.CREATURE_ENCOUNTERS.random()
        _activeCreatureEncounter.value = encounter
    }

    fun chooseEncounterOption(choiceIndex: Int) {
        val enc = _activeCreatureEncounter.value ?: return
        val choice = enc.choices.getOrNull(choiceIndex) ?: return

        awardXpAndPoints(choice.xpDelta, choice.pointsDelta, "Encountered ${enc.creatureName}")
        unlockBadge("badge_creature_companion")
        completeQuest("quest_creature_cuddle")

        _lastEncounterOutcome.value = choice.consequenceText
        _activeCreatureEncounter.value = null

        triggerOwlPost(
            title = "${enc.creatureName} Encounter!",
            message = choice.consequenceText,
            pointsChange = choice.pointsDelta,
            xpChange = choice.xpDelta,
            iconEmoji = "🐾"
        )
    }

    fun dismissCreatureEncounter() {
        _activeCreatureEncounter.value = null
    }

    fun dismissEncounterOutcome() {
        _lastEncounterOutcome.value = null
    }

    // --- Timetable & Classes ---
    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    fun attendClass(item: ClassScheduleItem) {
        awardXpAndPoints(xp = 35, points = 15, reason = "Attended ${item.subjectName}")
        completeQuest("quest_attend_class")
        advanceDailyQuests(QuestActionType.ATTEND_CLASS, item.locationId)

        if (item.locationId == "loc_potions") {
            unlockBadge("badge_snapples_least_fav")
        }
        triggerOwlPost(
            title = "Class Attended!",
            message = "You survived ${item.subjectName} with ${item.professor}! Earned 35 XP and 15 House Points.",
            pointsChange = 15,
            xpChange = 35,
            iconEmoji = "📖"
        )
    }

    // --- Gemini Chatbot (Multi-Turn with Role Selector) ---
    fun setChatbotRole(role: ChatbotRole) {
        _currentChatbotRole.value = role
    }

    fun clearChatHistory() {
        val role = _currentChatbotRole.value
        _chatMessages.value = listOf(
            ChatMessage(
                id = "init_${System.currentTimeMillis()}",
                senderName = role.title,
                text = "Conversation refreshed. I am ready to assist with ${role.description}!",
                isUser = false,
                modelUsed = role.modelName,
                roleEmoji = role.emoji
            )
        )
    }

    fun sendGhostQuestion(question: String) {
        if (question.isBlank()) return
        val role = _currentChatbotRole.value
        val userMsg = ChatMessage(
            id = "usr_${System.currentTimeMillis()}",
            senderName = _userProfile.value.studentName,
            text = question,
            isUser = true
        )
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMsg
        _isGhostThinking.value = true

        advanceDailyQuests(QuestActionType.CHAT_GHOST)

        viewModelScope.launch {
            val answer = GeminiService.askGeminiChat(
                history = currentHistory,
                userQuestion = question,
                role = role
            )
            val ghostMsg = ChatMessage(
                id = "bot_${System.currentTimeMillis()}",
                senderName = role.title,
                text = answer,
                isUser = false,
                modelUsed = role.modelName,
                roleEmoji = role.emoji,
                ghostMood = when {
                    answer.contains("Snapple") || role == ChatbotRole.COMPLEX_SCHOLAR -> "Indignant"
                    answer.contains("crack") || answer.contains("vertebrae") -> "Melodramatic"
                    role == ChatbotRole.FAST_PRANKSTER -> "Gleeful"
                    else -> "Perplexed"
                }
            )
            _chatMessages.value = _chatMessages.value + ghostMsg
            _isGhostThinking.value = false

            completeQuest("quest_ghost_consult")
            unlockBadge("badge_ghost_whisperer")
            awardXpAndPoints(xp = 15, points = 5, reason = "Consulted ${role.roleName}")
        }
    }

    // --- Veo 3 Video Generation Operations ---
    fun generateVeoFromText(prompt: String, aspectRatio: VeoAspectRatio) {
        _isVeoGenerating.value = true
        _veoStatusMessage.value = "Summoning Veo 3.1 Fast..."
        viewModelScope.launch {
            val result = VeoVideoService.generateVideoFromText(
                prompt = prompt,
                aspectRatio = aspectRatio,
                onProgressUpdate = { msg -> _veoStatusMessage.value = msg }
            )
            _veoLastResult.value = result
            _isVeoGenerating.value = false
            if (result.isSuccess) {
                awardXpAndPoints(xp = 50, points = 25, reason = "Generated Pensieve Vision with Veo")
                triggerOwlPost(
                    title = "Pensieve Video Manifested! 🎬",
                    message = "Veo 3.1 Fast (${aspectRatio.apiValue}) finished video generation! (+50 XP, +25 House Pts)",
                    pointsChange = 25,
                    xpChange = 50,
                    iconEmoji = "🎬"
                )
            }
        }
    }

    fun animateVeoFromImage(context: Context, imageUri: Uri, prompt: String, aspectRatio: VeoAspectRatio) {
        _isVeoGenerating.value = true
        _veoStatusMessage.value = "Preparing portrait for Veo 3.1 Fast..."
        viewModelScope.launch {
            val result = VeoVideoService.animateImageToVideo(
                context = context,
                imageUri = imageUri,
                prompt = prompt,
                aspectRatio = aspectRatio,
                onProgressUpdate = { msg -> _veoStatusMessage.value = msg }
            )
            _veoLastResult.value = result
            _isVeoGenerating.value = false
            if (result.isSuccess) {
                awardXpAndPoints(xp = 60, points = 30, reason = "Animated Living Portrait with Veo")
                unlockBadge("badge_portrait_animator")
                triggerOwlPost(
                    title = "Portrait Animated into Video! 🖼️",
                    message = "Veo 3.1 Fast animated your image into a living portrait! (+60 XP, +30 House Pts)",
                    pointsChange = 30,
                    xpChange = 60,
                    iconEmoji = "🖼️"
                )
            }
        }
    }

    fun dismissVeoResult() {
        _veoLastResult.value = null
    }

    // --- Gamification Helpers ---
    private fun awardXpAndPoints(xp: Int, points: Int, reason: String) {
        val cur = _userProfile.value
        val newXp = cur.xp + xp
        val newPoints = cur.housePoints + points
        val newLevel = (newXp / 100) + 1

        val title = when (newLevel) {
            1 -> "First-Year Wanderer"
            2 -> "Hallway Menace"
            3 -> "Junior Hexer"
            4 -> "Marauder Apprenticed"
            else -> "Arch-Mischief Master"
        }

        _userProfile.value = cur.copy(
            xp = newXp,
            housePoints = newPoints,
            level = newLevel,
            currentTitle = title
        )

        // Update house scoreboard
        val house = cur.house ?: House.GRYFFINDOOR
        val scores = _houseScores.value.toMutableMap()
        scores[house] = (scores[house] ?: 300) + points
        _houseScores.value = scores
    }

    private fun deductPoints(points: Int, reason: String) {
        val cur = _userProfile.value
        val newPoints = maxOf(0, cur.housePoints - points)
        _userProfile.value = cur.copy(housePoints = newPoints)

        val house = cur.house ?: House.GRYFFINDOOR
        val scores = _houseScores.value.toMutableMap()
        scores[house] = maxOf(0, (scores[house] ?: 300) - points)
        _houseScores.value = scores
    }

    private fun completeQuest(questId: String) {
        val completed = _userProfile.value.completedQuests + questId
        _userProfile.value = _userProfile.value.copy(completedQuests = completed)
    }

    private fun unlockBadge(badgeId: String) {
        if (!_userProfile.value.unlockedBadges.contains(badgeId)) {
            val unlocked = _userProfile.value.unlockedBadges + badgeId
            _userProfile.value = _userProfile.value.copy(unlockedBadges = unlocked)
            val badge = HogwashData.BADGES.find { it.id == badgeId }
            triggerOwlPost(
                title = "New Badge Unlocked!",
                message = "${badge?.iconEmoji ?: "🎖️"} ${badge?.name ?: "Badge"}: ${badge?.description ?: ""}",
                iconEmoji = "🎖️"
            )
        }
    }

    fun triggerOwlPost(
        title: String,
        message: String,
        pointsChange: Int = 0,
        xpChange: Int = 0,
        iconEmoji: String = "📜"
    ) {
        _currentOwlPost.value = OwlPostNotification(
            title = title,
            message = message,
            pointsChange = pointsChange,
            xpChange = xpChange,
            iconEmoji = iconEmoji
        )
    }

    fun dismissOwlPost() {
        _currentOwlPost.value = null
    }
    // --- Audio Transcription State ---
    private val _isTranscribing = MutableStateFlow(false)
    val isTranscribing: StateFlow<Boolean> = _isTranscribing.asStateFlow()

    private val _lastTranscription = MutableStateFlow("")
    val lastTranscription: StateFlow<String> = _lastTranscription.asStateFlow()

    fun transcribeIncantation(audioBytes: ByteArray) {
        _isTranscribing.value = true
        _lastTranscription.value = "Transcribing incantation with gemini-3.5-transcribe..."
        viewModelScope.launch {
            val result = GeminiService.transcribeAudio(audioBytes)
            _lastTranscription.value = result
            _isTranscribing.value = false
            if (result.isNotBlank() && !result.startsWith("API Key") && !result.startsWith("Transcription error")) {
                sendGhostQuestion("Incantation recorded: " + result + ". What is its magical effect?")
            }
        }
    }
}
