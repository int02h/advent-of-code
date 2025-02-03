package aoc2016

import AocDay2
import Input
import java.math.BigInteger
import java.security.MessageDigest
import java.util.PriorityQueue
import kotlin.math.max

class Day17 : AocDay2 {

    private lateinit var password: String

    private val md = MessageDigest.getInstance("MD5")
    private val digest = ByteArray(16)

    override fun readInput(input: Input) {
        password = input.asText()
    }

    override fun part1() {
        val queue = PriorityQueue<State> { s1, s2 -> s1.path.length - s2.path.length }
        queue += State(0, 0, "")

        while (queue.isNotEmpty()) {
            val state = queue.remove()
            val doorState = md5(password + state.path).take(4).map { it.digitToInt(16) > 10 }
            if (state.row == 3 && state.col == 3) {
                println(state.path)
                break
            }
            if (doorState[0] && state.row > 0) {
                queue.add(state.copy(row = state.row - 1, path = state.path + 'U'))
            }
            if (doorState[1] && state.row < 3) {
                queue.add(state.copy(row = state.row + 1, path = state.path + 'D'))
            }
            if (doorState[2] && state.col > 0) {
                queue.add(state.copy(col = state.col - 1, path = state.path + 'L'))
            }
            if (doorState[3] && state.col < 3) {
                queue.add(state.copy(col = state.col + 1, path = state.path + 'R'))
            }
        }
    }

    override fun part2() {
        val queue = PriorityQueue<State> { s1, s2 -> s2.path.length - s1.path.length }
        queue += State(0, 0, "")

        var best = 0

        while (queue.isNotEmpty()) {
            val state = queue.remove()
            val doorState = md5(password + state.path).take(4).map { it.digitToInt(16) > 10 }
            if (state.row == 3 && state.col == 3) {
                best = max(best, state.path.length)
                continue
            }
            if (doorState[0] && state.row > 0) {
                queue.add(state.copy(row = state.row - 1, path = state.path + 'U'))
            }
            if (doorState[1] && state.row < 3) {
                queue.add(state.copy(row = state.row + 1, path = state.path + 'D'))
            }
            if (doorState[2] && state.col > 0) {
                queue.add(state.copy(col = state.col - 1, path = state.path + 'L'))
            }
            if (doorState[3] && state.col < 3) {
                queue.add(state.copy(col = state.col + 1, path = state.path + 'R'))
            }
        }
        println(best)
    }

    private fun md5(value: String): String {
        md.update(value.toByteArray())
        md.digest(digest, 0, digest.size)
        return String.format("%032x", BigInteger(1, digest))
    }

    private data class State(
        val row: Int,
        val col: Int,
        val path: String
    )

}
