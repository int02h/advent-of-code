package aoc2015

import Input
import java.util.LinkedList

object Day9 {

    fun part1(input: Input) {
        println(getAllRoutes(input).minOf { it.totalDistance })
    }

    fun part2(input: Input) {
        println(getAllRoutes(input).maxOf { it.totalDistance })
    }

    private fun getAllRoutes(input: Input): List<Route> {
        val graph = Graph()
        input.asLines()
            .map { line -> line.split(' ') }
            .map { values ->
                val from = values[0]
                val to = values[2]
                val distance = values[4].toInt()
                graph.addConnection(from, to, distance)
            }
        val routes = mutableListOf<Route>()
        graph.nodes.forEach { node ->
            graph.getRoutes(node, graph.nodes.size, routes)
        }
        return routes
    }

    private class Graph {

        private val edges = mutableMapOf<String, MutableMap<String, Int>>()
        val nodes = mutableSetOf<String>()

        fun addConnection(from: String, to: String, distance: Int) {
            edges.getOrPut(from) { mutableMapOf() }[to] = distance
            edges.getOrPut(to) { mutableMapOf() }[from] = distance
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

    }

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

    private class Route(
        val route: List<String>,
        val totalDistance: Int
    )

}