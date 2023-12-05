package aoc2023

import AocDay
import Input

object Day4 : AocDay {

    override fun part1(input: Input) {
        val cards = parse(input)
        println(cards.sumOf { c -> c.getScore() })
    }

    override fun part2(input: Input) {
        val cards = parse(input).toMutableList()
        val counts = IntArray(cards.size) { 1 }
        cards.forEachIndexed { i, c ->
            val matchCount = c.getMatchCount()
            for (j in (i + 1)..(i + matchCount)) {
                counts[j] += counts[i]
            }
        }
        println(counts.sum())
    }

    private fun parse(input: Input): List<Card> =
        input.asLines().map { line ->
            val numbers = line.split(": ")[1]
            val (winning, player) = numbers.split(" | ").map { nums ->
                nums.split(" ").filter { it.trim().isNotEmpty() }.map { it.toInt() }
            }
            Card(winning.toSet(), player.toSet())
        }

    data class Card(val winningNumbers: Set<Int>, val playerNumbers: Set<Int>) {
        fun getScore(): Int {
            val count = getMatchCount()
            if (count == 0) {
                return 0
            }
            return 1 shl (count - 1)
        }

        fun getMatchCount(): Int = playerNumbers.count { winningNumbers.contains(it) }
    }
}