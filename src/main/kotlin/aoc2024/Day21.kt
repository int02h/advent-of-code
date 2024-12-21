package aoc2024

import AocDay2
import Input

class Day21 : AocDay2 {

    private lateinit var codes: List<String>

    private val numericKeypad = arrayOf(
        charArrayOf('7', '8', '9'),
        charArrayOf('4', '5', '6'),
        charArrayOf('1', '2', '3'),
        charArrayOf('.', '0', 'A'),
    )
    private val numericRouteMap = mutableMapOf<Pair<Char, Char>, List<String>>()

    private val directionalKeypad = arrayOf(
        charArrayOf('.', '^', 'A'),
        charArrayOf('<', 'v', '>'),
    )
    private val directionalRouteMap = mutableMapOf<Pair<Char, Char>, List<String>>()

    private val cache = mutableMapOf<State, Long>()

    override fun readInput(input: Input) {
        codes = input.asLines()
    }

    override fun part1() {
        buildMaps()
        println(codes.sumOf { calculate(it, 2) })
    }

    override fun part2() {
        buildMaps()
        println(codes.sumOf { calculate(it, 25) })
    }

    private fun calculate(target: String, robotCount: Int): Long {
        var prevCh = 'A'
        var result = 0L
        for (ch in target) {
            result += dp(State(prevCh, ch), robotCount)
            prevCh = ch
        }
        return (target.dropLast(1).toLong() * result)
    }

    private fun buildMaps() {
        val result = mutableListOf<String>()
        for (from in "0123456789A") {
            for (to in "0123456789A") {
                result.clear()
                getRoutes(numericKeypad, from, to, result)
                numericRouteMap[from to to] = ArrayList(result)
            }
        }
        for (from in "<>^vA") {
            for (to in "<>^vA") {
                result.clear()
                getRoutes(directionalKeypad, from, to, result)
                directionalRouteMap[from to to] = ArrayList(result)
            }
        }
    }

    private fun getRoutes(
        keypad: Array<CharArray>,
        from: Char,
        to: Char,
        result: MutableList<String>,
        visited: String = "",
        localPath: String = "",
    ) {
        if (from == to) {
            result += localPath + 'A'
            return
        }
        val row = keypad.indexOfFirst { it.contains(from) }
        val col = keypad[row].indexOf(from)
        for (d in Direction.values()) {
            val ch = keypad.getOrNull(row + d.dRow)?.getOrNull(col + d.dCol)
            if (ch != null && ch != '.' && !visited.contains(ch)) {
                getRoutes(keypad, ch, to, result, visited + ch, localPath + d.value)
            }
        }
    }

    private fun dp(state: State, robotCount: Int): Long {
        if (state.robotId == robotCount + 1) {
            return 1
        }
        if (cache.containsKey(state)) return cache.getValue(state)
        val routeMap = if (state.robotId == 0) numericRouteMap else directionalRouteMap
        val routes = routeMap.getValue(state.from to state.to)
        val best = routes.minOfOrNull { route ->
            var sum = 0L
            var prevCh = 'A'
            for (ch in route) {
                sum += dp(State(prevCh, ch, state.robotId + 1), robotCount)
                prevCh = ch
            }
            sum
        } ?: Long.MAX_VALUE
        cache[state] = best
        return best
    }

    private data class State(val from: Char, val to: Char, val robotId: Int = 0)

    enum class Direction(val dRow: Int, val dCol: Int, val value: Char) {
        UP(-1, 0, '^'),
        DOWN(1, 0, 'v'),
        LEFT(0, -1, '<'),
        RIGHT(0, 1, '>'),
    }
}