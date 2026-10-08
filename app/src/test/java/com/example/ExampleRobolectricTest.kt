package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HogwashData
import com.example.model.House
import com.example.model.QuestActionType
import com.example.service.DailyQuestManager
import com.example.service.DijkstraRouter
import com.example.service.GeminiService
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Hairy Porter", appName)
    }

    @Test
    fun `dijkstra finds shortest path between castle locations`() {
        val path = DijkstraRouter.findShortestPath("loc_great_hall", "loc_potions")
        assertNotNull(path)
        assertTrue(path.nodeIds.contains("loc_great_hall"))
        assertTrue(path.nodeIds.contains("loc_potions"))
        assertTrue(path.totalDistance > 0)
    }

    @Test
    fun `dijkstra routes to dangerous webby hollow in forbidden forest`() {
        val path = DijkstraRouter.findShortestPath("loc_great_hall", "loc_webby_hollow")
        assertNotNull(path)
        assertTrue(path.nodeIds.contains("loc_webby_hollow"))
    }

    @Test
    fun `staircase shift creates reroute`() {
        val shift = DijkstraRouter.triggerStaircaseShift("loc_great_hall", "loc_astronomy_tower")
        assertTrue(shift.didStaircaseMove)
        assertNotNull(shift.moveEventText)
        assertTrue(shift.path.nodeIds.isNotEmpty())
    }

    @Test
    fun `ghost guide fallback answers accurately for potions`() {
        val answer = GeminiService.getLocalFallbackAnswer("Where is the potions classroom?")
        assertTrue(answer.contains("Dungeon") || answer.contains("Snapple"))
    }

    @Test
    fun `all four houses are properly configured`() {
        assertEquals(4, House.values().size)
        assertTrue(HogwashData.SORTING_QUESTIONS.size >= 7)
    }

    @Test
    fun `daily quest manager generates randomized tasks`() {
        val quests = DailyQuestManager.generateRandomDailyQuests(4)
        assertEquals(4, quests.size)
        assertTrue(DailyQuestManager.QUEST_TEMPLATE_POOL.any { it.title.contains("Butterbeer") })
        assertTrue(DailyQuestManager.QUEST_TEMPLATE_POOL.any { it.title.contains("Archives") })
    }

    @Test
    fun `daily quest manager advances brewing and library tasks`() {
        val butterbeerQuest = DailyQuestManager.QUEST_TEMPLATE_POOL.first { it.actionType == QuestActionType.BREW_INTERACTIVE }
        val libraryQuest = DailyQuestManager.QUEST_TEMPLATE_POOL.first { it.targetLocationId == "loc_library" }

        val activeQuests = listOf(butterbeerQuest, libraryQuest)

        // Advance brewing
        val brewResult = DailyQuestManager.advanceQuests(activeQuests, QuestActionType.BREW_INTERACTIVE)
        assertEquals(1, brewResult.newlyCompleted.size)
        assertTrue(brewResult.totalXpEarned > 0)
        assertTrue(brewResult.totalPointsEarned > 0)

        // Advance library visit
        val libResult = DailyQuestManager.advanceQuests(activeQuests, QuestActionType.VISIT_LOCATION, "loc_library")
        assertEquals(1, libResult.newlyCompleted.size)
        assertTrue(libResult.newlyCompleted.first().id == libraryQuest.id)
    }

    @Test
    fun `creature encounters exist for haggards hut and forest`() {
        val hutEnc = HogwashData.CREATURE_ENCOUNTERS.find { it.locationId == "loc_haggard_hut" }
        assertNotNull(hutEnc)
        assertEquals("Blast-Ended Screwtop", hutEnc?.creatureName)

        val forestEnc = HogwashData.CREATURE_ENCOUNTERS.find { it.locationId == "loc_webby_hollow" }
        assertNotNull(forestEnc)
        assertEquals("Aragog's Cousin Greg", forestEnc?.creatureName)
    }

    @Test
    fun `navigation tab includes dashboard as primary entry`() {
        assertEquals(NavigationTab.DASHBOARD, NavigationTab.values().first())
        assertEquals("nav_tab_dashboard", NavigationTab.DASHBOARD.testTag)
    }
}
