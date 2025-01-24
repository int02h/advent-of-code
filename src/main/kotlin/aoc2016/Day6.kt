package aoc2016

import AocDay2
import Input
import util.Counter

class Day6 : AocDay2 {

    private lateinit var signal: List<String>

    override fun readInput(input: Input) {
        signal = input.asLines()
    }

    override fun part1() {
        val counters = Array(signal[0].length) { Counter<Char>() }
        signal.forEach { msg ->
            msg.forEachIndexed { index, ch ->
                counters[index].count(ch)
            }
        }

        counters.forEach {
            print(it.getGreatestCountKey())
        }
        println()
    }

    override fun part2() {

        val counters = Array(signal[0].length) { Counter<Char>() }
        signal.forEach { msg ->
            msg.forEachIndexed { index, ch ->
                counters[index].count(ch)
            }
        }

        counters.forEach {
            print(it.getLeastCountKey())
        }
        println()
    }
}