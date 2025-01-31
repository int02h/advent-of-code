package util

import java.util.PriorityQueue

class Maze(
    private val dataProvider: DataProvider
) {

    fun findShortestRoute(from: Point, to: Point): List<Point> {
        val visited = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<RoutePoint> { p1, p2 -> p1.routeLength - p2.routeLength }
        queue.add(RoutePoint(from, 0))

        while (queue.isNotEmpty()) {
            val routePoint = queue.remove()
            val v = visited[routePoint.point]
            if (v != null && v <= routePoint.routeLength) {
                continue
            }
            visited[routePoint.point] = routePoint.routeLength
            if (routePoint.point == to) {
                return buildRoute(from, to, visited)
            }
            if (dataProvider.isEmptyCell(routePoint.point.up)) queue.add(routePoint.up())
            if (dataProvider.isEmptyCell(routePoint.point.down)) queue.add(routePoint.down())
            if (dataProvider.isEmptyCell(routePoint.point.left)) queue.add(routePoint.left())
            if (dataProvider.isEmptyCell(routePoint.point.right)) queue.add(routePoint.right())
        }
        return emptyList()
    }

    fun findShortestRouteLength(from: Point, to: Point): Int {
        val visited = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<RoutePoint> { p1, p2 -> p1.routeLength - p2.routeLength }
        queue.add(RoutePoint(from, 0))

        while (queue.isNotEmpty()) {
            val routePoint = queue.remove()
            val v = visited[routePoint.point]
            if (v != null && v <= routePoint.routeLength) {
                continue
            }
            visited[routePoint.point] = routePoint.routeLength
            if (routePoint.point == to) {
                return routePoint.routeLength
            }
            if (dataProvider.isEmptyCell(routePoint.point.up)) queue.add(routePoint.up())
            if (dataProvider.isEmptyCell(routePoint.point.down)) queue.add(routePoint.down())
            if (dataProvider.isEmptyCell(routePoint.point.left)) queue.add(routePoint.left())
            if (dataProvider.isEmptyCell(routePoint.point.right)) queue.add(routePoint.right())
        }
        return Int.MAX_VALUE
    }

    fun findLongestRoute(from: Point): List<Point> {
        val visited = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<RoutePoint> { p1, p2 -> p2.routeLength - p1.routeLength }
        queue.add(RoutePoint(from, 0))

        while (queue.isNotEmpty()) {
            val routePoint = queue.remove()
            val v = visited[routePoint.point]
            if (v != null) {
                continue
            }
            visited[routePoint.point] = routePoint.routeLength
            if (dataProvider.isEmptyCell(routePoint.point.up)) queue.add(routePoint.up())
            if (dataProvider.isEmptyCell(routePoint.point.down)) queue.add(routePoint.down())
            if (dataProvider.isEmptyCell(routePoint.point.left)) queue.add(routePoint.left())
            if (dataProvider.isEmptyCell(routePoint.point.right)) queue.add(routePoint.right())
        }

        val mostDistantPoint = visited.maxBy { it.value }.key
        return buildRoute(from, mostDistantPoint, visited)
    }

    fun findAllRoutesOfLength(from: Point, length: Int): List<List<Point>> {
        val visited = mutableSetOf<Point>()
        val result = mutableListOf<List<Point>>()
        val routeList = mutableListOf(from)

        fun getAllRoutes(p: Point) {
            if (routeList.size == length + 1) {
                result += routeList.toMutableList()
                return
            }

            visited += p
            if (dataProvider.isEmptyCell(p.up)) {
                if (!visited.contains(p.up)) {
                    routeList += p.up
                    getAllRoutes(p.up)
                    routeList -= p.up
                }
            }
            if (dataProvider.isEmptyCell(p.down)) {
                if (!visited.contains(p.down)) {
                    routeList += p.down
                    getAllRoutes(p.down)
                    routeList -= p.down
                }
            }
            if (dataProvider.isEmptyCell(p.left)) {
                if (!visited.contains(p.left)) {
                    routeList += p.left
                    getAllRoutes(p.left)
                    routeList -= p.left
                }
            }
            if (dataProvider.isEmptyCell(p.right)) {
                if (!visited.contains(p.right)) {
                    routeList += p.right
                    getAllRoutes(p.right)
                    routeList -= p.right
                }
            }
            visited -= p
        }

        getAllRoutes(from)
        return result
    }

    private fun buildRoute(from: Point, to: Point, visited: Map<Point, Int>): List<Point> {
        val route = mutableListOf<Point>()
        var length = visited.getValue(to)
        var p = to

        while (p != from) {
            route += p
            if (visited[p.up] == length - 1) {
                p = p.up
                length--
                continue
            }
            if (visited[p.down] == length - 1) {
                p = p.down
                length--
                continue
            }
            if (visited[p.left] == length - 1) {
                p = p.left
                length--
                continue
            }
            if (visited[p.right] == length - 1) {
                p = p.right
                length--
                continue
            }
            error("Something went wrong")
        }
        route += from
        return route.reversed()
    }

    private data class RoutePoint(val point: Point, val routeLength: Int) {
        fun up(): RoutePoint = RoutePoint(point.up, routeLength + 1)
        fun down(): RoutePoint = RoutePoint(point.down, routeLength + 1)
        fun left(): RoutePoint = RoutePoint(point.left, routeLength + 1)
        fun right(): RoutePoint = RoutePoint(point.right, routeLength + 1)
    }

    data class Point(val x: Int, val y: Int) {
        val up: Point by lazy { copy(y = y - 1) }
        val down: Point by lazy { copy(y = y + 1) }
        val left: Point by lazy { copy(x = x - 1) }
        val right: Point by lazy { copy(x = x + 1) }
    }

    fun interface DataProvider {
        fun isEmptyCell(point: Point): Boolean
    }
}