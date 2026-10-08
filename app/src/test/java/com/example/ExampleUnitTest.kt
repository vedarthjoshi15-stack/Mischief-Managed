package com.example

import com.example.service.ChatbotRole
import com.example.service.DijkstraRouter
import com.example.service.VeoAspectRatio
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDijkstraPathfinding() {
        val pathResult = DijkstraRouter.findShortestPath("loc_great_hall", "loc_potions")
        assertNotNull(pathResult.path)
        assertTrue(pathResult.path!!.nodes.isNotEmpty())
        assertEquals("loc_great_hall", pathResult.path!!.nodes.first().id)
        assertEquals("loc_potions", pathResult.path!!.nodes.last().id)
    }

    @Test
    fun testVeoAspectRatios() {
        assertEquals("16:9", VeoAspectRatio.LANDSCAPE.apiValue)
        assertEquals("9:16", VeoAspectRatio.PORTRAIT.apiValue)
    }

    @Test
    fun testChatbotRolesAndModels() {
        assertEquals("gemini-3.5-flash", ChatbotRole.GENERAL_GUIDE.modelName)
        assertEquals("gemini-3.1-pro-preview", ChatbotRole.COMPLEX_SCHOLAR.modelName)
        assertEquals("gemini-3.1-flash-lite-preview", ChatbotRole.FAST_PRANKSTER.modelName)
    }
}
