package aoc2024

import AocDay
import Input
import java.util.regex.Pattern
import kotlin.math.min
import kotlin.math.roundToLong

object Day13 : AocDay {

    override fun part1(input: Input) {
        val machines = readInput(input)
        println(
            machines.sumOf {
                val result = it.part1()
                if (result == Int.MAX_VALUE) 0 else result
            }
        )
    }

    override fun part2(input: Input) {
        val machines = readInput(input)
        println(
            machines.sumOf { it.part2() }
        )
    }

    private fun readInput(input: Input): List<ClawMachine> {
        val pattern = Pattern.compile("(\\d+)")
        return input.asText()
            .split("\n\n")
            .map {
                val values = pattern.matcher(it).results().toList().map { res -> res.group().toLong() }
                ClawMachine(
                    btnA = Command(values[0], values[1]),
                    btnB = Command(values[2], values[3]),
                    prizeX = values[4],
                    prizeY = values[5],
                )
            }
    }

    private data class ClawMachine(
        val btnA: Command,
        val btnB: Command,
        val prizeX: Long,
        val prizeY: Long
    ) {
        private val cache = mutableMapOf<State, Int>()

        fun part1(state: State = State()): Int {
            if (state.x == prizeX && state.y == prizeY) {
                return state.tokenCount
            }
            if (state.x > prizeX || state.y > prizeY) {
                return Int.MAX_VALUE
            }
            val cached = cache[state]
            if (cached != null) {
                return cached
            }
            val p1 = part1(
                State(
                    x = state.x + btnA.dx,
                    y = state.y + btnA.dy,
                    tokenCount = state.tokenCount + 3
                )
            )
            val p2 = part1(
                State(
                    x = state.x + btnB.dx,
                    y = state.y + btnB.dy,
                    tokenCount = state.tokenCount + 1
                )
            )
            val best = min(p1, p2)
            cache[state] = best
            return best
        }

        fun part2(): Long {
            val yp = prizeY + 10000000000000
            val xp = prizeX + 10000000000000
            val xa = btnA.dx
            val ya = btnA.dy
            val xb = btnB.dx
            val yb = btnB.dy
            val cb = 1.0 * (yp * xa - xp * ya) / (yb * xa - xb * ya)
            val ca = 1.0 * (xp - cb * xb) / xa
            if (ca < 0 || cb < 0) {
                return 0
            }
            if (ca % 1 != 0.0 || cb % 1 != 0.0) {
                return 0
            }
            return 3 * ca.roundToLong() + cb.roundToLong()
        }

        private data class State(val x: Long = 0, val y: Long = 0, val tokenCount: Int = 0)
    }

    private data class Command(val dx: Long, val dy: Long)

}