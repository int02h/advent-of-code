package aoc2016

import AocDay2
import Input
import util.Maze
import util.Maze.Point
import util.get

class Day24 : AocDay2 {

    private lateinit var map: List<String>
    private lateinit var startPoint: Point
    private val poiList = mutableListOf<Point>()
    private val routeLengthMap = mutableMapOf<Pair<Point, Point>, Int>()

    override fun readInput(input: Input) {
        map = input.asLines()
    }

    override fun part1() {
        prepareData()
        println(dp(State(startPoint, poiList.toSet() - startPoint)))
    }

    override fun part2() {
        prepareData()
        println(dp(State(startPoint, poiList.toSet() - startPoint), isPart2 = true))
    }

    private fun prepareData() {
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (ch.isDigit()) {
                    poiList += Point(x, y)
                    if (ch == '0') {
                        startPoint = Point(x, y)
                    }
                }
            }
        }

        val maze = Maze { p -> map[p] != '#' }
        for (i in poiList.indices) {
            for (j in (i + 1)..poiList.lastIndex) {
                val routeLength = maze.findShortestRouteLength(poiList[i], poiList[j])
                routeLengthMap[poiList[i] to poiList[j]] = routeLength
                routeLengthMap[poiList[j] to poiList[i]] = routeLength
            }
        }
    }

    private fun dp(state: State, isPart2: Boolean = false): Int {
        if (state.nonVisitedPoi.isEmpty()) {
            return state.routeLength + if (isPart2) routeLengthMap.getValue(state.currentPoi to startPoint) else 0
        }
        return state.nonVisitedPoi.minOf { poi ->
            dp(
                State(
                    currentPoi = poi,
                    nonVisitedPoi = state.nonVisitedPoi - poi,
                    routeLength = state.routeLength + routeLengthMap.getValue(state.currentPoi to poi)
                ),
                isPart2
            )
        }
    }

    private data class State(
        val currentPoi: Point,
        val nonVisitedPoi: Set<Point>,
        val routeLength: Int = 0
    )
}

