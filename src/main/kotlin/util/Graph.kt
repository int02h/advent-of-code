package util

import java.util.LinkedList
import kotlin.collections.plusAssign

class Graph {

    val edges = mutableMapOf<String, MutableMap<String, Int>>()
    val nodes = mutableSetOf<String>()

    fun addConnection(from: String, to: String, distance: Int) {
        edges.getOrPut(from) { mutableMapOf() }[to] = distance
        edges.getOrPut(to) { mutableMapOf() }[from] = distance
        nodes += from
        nodes += to
    }

    fun addOrientedConnection(from: String, to: String, distance: Int) {
        edges.getOrPut(from) { mutableMapOf() }[to] = distance
        nodes += from
        nodes += to
    }

    fun getRoutes(from: String, length: Int, allRoutes: MutableList<Route>) {
        val stack = LinkedList<StackItem>()
        stack += StackItem(node = from, route = listOf(from), totalDistance = 0)

        while (stack.isNotEmpty()) {
            val item = stack.removeFirst()
            if (item.route.size == length) {
                allRoutes += Route(item.route, item.totalDistance)
                continue
            }
            edges.getValue(item.node).forEach { (to, distance) ->
                if (!item.route.contains(to)) {
                    stack.addLast(item.go(to, distance))
                }
            }
        }
    }

    fun getAllRoutes(from: String, to: String): List<Route> {
        val stack = LinkedList<StackItem>()
        stack += StackItem(node = from, route = listOf(from), totalDistance = 0)
        val allRoutes = mutableListOf<Route>()

        while (stack.isNotEmpty()) {
            val item = stack.removeFirst()
            if (item.route.last() == to) {
                allRoutes += Route(item.route, item.totalDistance)
                continue
            }
            edges[item.node]?.forEach { (to, distance) ->
                if (!item.route.contains(to)) {
                    stack.addLast(item.go(to, distance))
                }
            }
        }

        return allRoutes
    }

    data class Route(
        val route: List<String>,
        val totalDistance: Int
    )

    private class StackItem(
        val node: String,
        val route: List<String>,
        val totalDistance: Int
    ) {

        fun go(to: String, distance: Int): StackItem = StackItem(
            node = to,
            route = route + to,
            totalDistance = totalDistance + distance
        )
    }
}
