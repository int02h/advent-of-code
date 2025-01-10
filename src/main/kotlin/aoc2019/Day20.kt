package aoc2019

import AocDay2
import Input
import java.util.PriorityQueue

class Day20 : AocDay2 {

    private lateinit var map: List<String>
    private val portals = mutableMapOf<String, MutableSet<PortalPoint>>()
    private var width = 0
    private var height = 0
    private var isPart2 = false

    override fun readInput(input: Input) {
        map = input.asLines()
        width = map[0].length
        height = map.size

        val rotated = Array(width) { CharArray(height) { ' ' } }
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                rotated[width - x - 1][y] = ch
            }
        }

        map.forEachIndexed { y, line ->
            var x = 0
            while (x < line.length - 1) {
                if (line[x] in 'A'..'Z' && line[x + 1] in 'A'..'Z') {
                    val portal = "" + line[x] + line[x + 1]
                    val set = portals.getOrPut(portal) { mutableSetOf() }
                    if ((line.getOrNull(x - 1) ?: ' ') == ' ') {
                        set += PortalPoint(x + 2, y)
                    } else {
                        set += PortalPoint(x - 1, y)
                    }
                    x++
                }
                x++
            }
        }

        rotated.forEachIndexed { x, column ->
            var y = 0
            while (y < column.size) {
                if (column[y] in 'A'..'Z' && column[y + 1] in 'A'..'Z') {
                    val portal = "" + column[y] + column[y + 1]
                    val set = portals.getOrPut(portal) { mutableSetOf() }
                    if ((column.getOrNull(y - 1) ?: ' ') == ' ') {
                        set += PortalPoint(width - x - 1, y + 2)
                    } else {
                        set += PortalPoint(width - x - 1, y - 1)
                    }
                    y++
                }
                y++
            }
        }
    }

    override fun part1() {
        val from = portals.getValue("AA").first()
        val to = portals.getValue("ZZ").first()
        println(findShortestRoute(from.toPoint(level = 0, "AA"), to.toPoint(level = 0, "")))
    }

    override fun part2() {
        isPart2 = true
        val from = portals.getValue("AA").first()
        val to = portals.getValue("ZZ").first()
        println(findShortestRoute(from.toPoint(level = 0, "AA"), to.toPoint(level = 0, "ZZ")))
    }

    private fun findShortestRoute(from: Point, to: Point): Int {
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
            move(routePoint, routePoint.up())?.let(queue::add)
            move(routePoint, routePoint.down())?.let(queue::add)
            move(routePoint, routePoint.left())?.let(queue::add)
            move(routePoint, routePoint.right())?.let(queue::add)
        }
        return Int.MAX_VALUE
    }

    private fun move(from: RoutePoint, to: RoutePoint): RoutePoint? {
        if (isPart2 && from.point.level == 0 && to.point.isOuter()) {
            return null
        }
        val ch = map[to.point.y][to.point.x]
        if (ch == '.') {
            return to
        }
        if (ch in 'A'..'Z') {
            val dx = to.point.x - from.point.x
            val dy = to.point.y - from.point.y
            var portal = "$ch${map[to.point.y + dy][to.point.x + dx]}"
            if (!portals.containsKey(portal)) {
                portal = portal.reversed()
            }
            if (portal == "AA" || portal == "ZZ") {
                if (from.point.level > 0) {
                    return null
                } else {
                    return RoutePoint(
                        portals.getValue(portal).first().toPoint(from.point.level, to.point.debug + "-" + portal),
                        to.routeLength
                    )
                }
            }
            val dLevel = if (isPart2) {
                if (to.point.isOuter()) -1 else 1
            } else {
                0
            }
            val target = (HashSet(portals.getValue(portal)) - from.point.toPortalPoint()).first()
            return RoutePoint(
                target.toPoint(to.point.level + dLevel, to.point.debug + "-" + portal),
                to.routeLength
            )
        }
        return null
    }

    private fun Point.isOuter(): Boolean {
        return x == 1 || x == width - 2 || y == 1 || y == height - 2
    }

    private data class RoutePoint(val point: Point, val routeLength: Int) {
        fun up(): RoutePoint = RoutePoint(point.up, routeLength + 1)
        fun down(): RoutePoint = RoutePoint(point.down, routeLength + 1)
        fun left(): RoutePoint = RoutePoint(point.left, routeLength + 1)
        fun right(): RoutePoint = RoutePoint(point.right, routeLength + 1)
    }

    private data class PortalPoint(val x: Int, val y: Int) {
        fun toPoint(level: Int, debug: String): Point = Point(x = x, y = y, level = level, debug = debug)
    }

    private data class Point(val x: Int, val y: Int, val level: Int) {
        val up: Point by lazy { copy(y = y - 1).also { it.debug = debug } }
        val down: Point by lazy { copy(y = y + 1).also { it.debug = debug } }
        val left: Point by lazy { copy(x = x - 1).also { it.debug = debug } }
        val right: Point by lazy { copy(x = x + 1).also { it.debug = debug } }

        var debug = ""

        constructor(x: Int, y: Int, level: Int, debug: String) : this(x, y, level) {
            this.debug = debug
        }

        fun toPortalPoint(): PortalPoint = PortalPoint(x = x, y = y)
    }
}