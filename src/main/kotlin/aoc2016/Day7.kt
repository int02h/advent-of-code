package aoc2016

import AocDay2
import Input

class Day7 : AocDay2 {

    private lateinit var lines: List<String>

    override fun readInput(input: Input) {
        lines = input.asLines()
    }

    override fun part1() {
        println(lines.count(::isSupportTls))
    }

    override fun part2() {
        println(lines.count(::isSupportSsl))
    }

    private fun isSupportTls(address: String): Boolean {
        val indices = mutableListOf<Int>()
        val brackets = mutableListOf<IntRange>()
        var startIndex = 0
        address.windowed(4, 1, false)
            .forEachIndexed { index, str ->
                if (str[0] == str[3] && str[1] == str[2] && str[0] != str[1]) {
                    indices += index
                }
                if (str[0] == '[') {
                    startIndex = index
                } else if (str[3] == ']') {
                    brackets += startIndex..(index + 3)
                }
            }
        if (indices.isEmpty()) {
            return false
        }
        return indices.none { i -> brackets.any { b-> i in b} }
    }

    private fun isSupportSsl(address: String): Boolean {
        val brackets = mutableListOf<IntRange>()
        var startIndex = 0
        address.forEachIndexed { index, ch ->
            if (ch == '[') {
                startIndex = index
            } else if (ch == ']') {
                brackets += startIndex..index
            }
        }

        val inBrackets = mutableListOf<String>()
        val outBrackets = mutableListOf<String>()
        address.windowed(3, 1, false)
            .forEachIndexed { index, str ->
                if (!str.contains('[') && !str.contains(']')) {
                    if (brackets.any { b -> index in b }) {
                        if (str[0] == str[2] && str[0] != str[1]) {
                            inBrackets += str
                        }
                    } else {
                        if (str[0] == str[2] && str[0] != str[1]) {
                            outBrackets += "${str[1]}${str[0]}${str[1]}"
                        }
                    }
                }
            }
        return outBrackets.any { ob -> inBrackets.any { ib -> ib == ob} }
    }
}