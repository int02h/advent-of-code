package aoc2016

import AocDay2
import Input

class Day9 : AocDay2 {

    private lateinit var compressedText: String

    override fun readInput(input: Input) {
        compressedText = input.asText().trim()
    }

    override fun part1() {
        println(getDecompressedLength(compressedText))
    }

    override fun part2() {
        println(getDecompressedLength(compressedText, isPart2 = true))
    }

    private fun getDecompressedLength(value: String, isPart2: Boolean = false): Long {
        val list = value.toMutableList()
        var isMarker = false
        var marker = ""
        var result = 0L
        while (list.isNotEmpty()) {
            val ch = list.removeFirst()
            when (ch) {
                '(' -> {
                    marker = ""
                    isMarker = true
                }
                ')' -> {
                    val (length, count) = marker.split('x').map { it.toInt() }
                    var buffer = ""
                    repeat(length) {
                        buffer += list.removeFirst()
                    }
                    if (isPart2) {
                        result += count * getDecompressedLength(buffer, isPart2 = true)
                    } else {
                        result += count * buffer.length
                    }
                    isMarker = false
                }
                else -> {
                    if (isMarker) {
                        marker += ch
                    } else {
                        result++
                    }
                }
            }
        }
        return result
    }
}