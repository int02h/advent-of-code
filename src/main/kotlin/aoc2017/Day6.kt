package aoc2017

import AocDay2
import Input
import util.findAllNumbers

class Day6 : AocDay2 {

    private lateinit var banks: MutableList<Int>

    override fun readInput(input: Input) {
        banks = input.asText().findAllNumbers().toMutableList()
    }

    override fun part1() {
        val visited = mutableSetOf<String>()
        while (true) {
            var value = banks.max()
            var index = banks.indexOf(value)
            banks[index] = 0
            while (value > 0) {
                index = (index + 1) % banks.size
                banks[index]++
                value--
            }
            val state = banks.joinToString()
            if (visited.contains(state)) {
                break
            } else {
                visited += state
            }
        }
        println(visited.size + 1)
    }

    override fun part2() {
        val visited = mutableListOf<String>()
        while (true) {
            var value = banks.max()
            var index = banks.indexOf(value)
            banks[index] = 0
            while (value > 0) {
                index = (index + 1) % banks.size
                banks[index]++
                value--
            }
            val state = banks.joinToString()
            val visitedIndex = visited.indexOf(state)
            if (visitedIndex >= 0) {
                println(visited.size - visitedIndex)
                break
            } else {
                visited += state
            }
        }
    }

}