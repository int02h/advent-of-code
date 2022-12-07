package aoc2022

import Input

object Day6 {
    fun part1(input: Input) {
        findMarker(input, 4)
    }

    fun part2(input: Input) {
        findMarker(input, 14)
    }

    private fun findMarker(input: Input, markerLength: Int) {
        val buffer = input.asText()
        val lastFourChar = buffer.substring(0, markerLength).toMutableList()
        for (i in markerLength..buffer.lastIndex) {
            if (isAllCharsDifferent(lastFourChar)) {
                println(i)
                return
            } else {
                lastFourChar.removeAt(0)
                lastFourChar += buffer[i]
            }
        }
    }

    private fun isAllCharsDifferent(chars: List<Char>): Boolean = chars.toSet().size == chars.size
}