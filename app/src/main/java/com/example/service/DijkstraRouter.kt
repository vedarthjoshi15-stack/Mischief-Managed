package com.example.service

import com.example.data.HogwashData
import com.example.model.MapEdge
import com.example.model.NavigationPath
import java.util.PriorityQueue

object DijkstraRouter {

    data class PathResult(
        val path: NavigationPath,
        val didStaircaseMove: Boolean = false,
        val moveEventText: String? = null
    )

    fun findShortestPath(
        startId: String,
        endId: String,
        staircaseWeightMultiplier: Int = 1,
        blockedEdgePair: Pair<String, String>? = null
    ): NavigationPath {
        if (startId == endId) {
            val node = HogwashData.LOCATIONS.find { it.id == startId }
            return NavigationPath(
                nodeIds = listOf(startId),
                totalDistance = 0,
                routeDescription = "You are already standing at ${node?.name ?: "your destination"}!"
            )
        }

        // Build adjacency map (undirected graph)
        val adjMap = mutableMapOf<String, MutableList<Pair<String, Int>>>()
        HogwashData.LOCATIONS.forEach { adjMap[it.id] = mutableListOf() }

        for (edge in HogwashData.EDGES) {
            val isBlocked = blockedEdgePair != null && (
                (edge.fromId == blockedEdgePair.first && edge.toId == blockedEdgePair.second) ||
                (edge.fromId == blockedEdgePair.second && edge.toId == blockedEdgePair.first)
            )
            if (isBlocked) continue

            val weight = if (edge.isStaircase) edge.weight * staircaseWeightMultiplier else edge.weight
            adjMap[edge.fromId]?.add(Pair(edge.toId, weight))
            adjMap[edge.toId]?.add(Pair(edge.fromId, weight))
        }

        // Dijkstra's algorithm
        val dist = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }
        val prev = mutableMapOf<String, String>()
        val pq = PriorityQueue<Pair<String, Int>>(compareBy { it.second })

        dist[startId] = 0
        pq.add(Pair(startId, 0))

        while (pq.isNotEmpty()) {
            val (current, currentDist) = pq.poll() ?: break

            if (current == endId) break
            if (currentDist > (dist[current] ?: Int.MAX_VALUE)) continue

            val neighbors = adjMap[current] ?: emptyList()
            for ((neighbor, edgeWeight) in neighbors) {
                val newDist = currentDist + edgeWeight
                if (newDist < (dist[neighbor] ?: Int.MAX_VALUE)) {
                    dist[neighbor] = newDist
                    prev[neighbor] = current
                    pq.add(Pair(neighbor, newDist))
                }
            }
        }

        // Reconstruct path
        val path = mutableListOf<String>()
        var curr: String? = endId
        while (curr != null) {
            path.add(0, curr)
            curr = prev[curr]
        }

        // Check if path is valid
        if (path.isEmpty() || path.first() != startId) {
            return NavigationPath(
                nodeIds = listOf(startId, endId),
                totalDistance = 999,
                routeDescription = "No clear path found through the shifting enchanted stone!"
            )
        }

        val totalDist = dist[endId] ?: 0
        val names = path.map { id -> HogwashData.LOCATIONS.find { it.id == id }?.name ?: id }
        val description = "Route: " + names.joinToString(" ➔ ")

        return NavigationPath(
            nodeIds = path,
            totalDistance = totalDist,
            routeDescription = description
        )
    }

    fun triggerStaircaseShift(
        startId: String,
        endId: String
    ): PathResult {
        val shiftReasons = listOf(
            "The Great Marble Flight just rotated 90 degrees out of sheer spite toward late students!",
            "Sir Nicholas leaned on the banister, causing the entire landing to swing toward the dungeons!",
            "Peeves-ish dumped slippery dragon dung on the central stairs, forcing an emergency detour!",
            "The 142nd staircase has decided to lead into an empty broom closet for the next 10 minutes!"
        )
        val reason = shiftReasons.random()

        // Recalculate with heavy staircase penalty or blocking the primary staircase
        val reroutedPath = findShortestPath(
            startId = startId,
            endId = endId,
            staircaseWeightMultiplier = 4,
            blockedEdgePair = Pair("loc_staircase", "loc_astronomy_tower")
        )

        return PathResult(
            path = reroutedPath,
            didStaircaseMove = true,
            moveEventText = reason
        )
    }
}
