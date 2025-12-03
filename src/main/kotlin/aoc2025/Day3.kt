package aoc2025

import AocDay2
import Input

class Day3 : AocDay2 {

    private lateinit var banks: List<String>

    override fun readInput(input: Input) {
        banks = input.asLines()
    }

    override fun part1() {
        println(
            banks.sumOf { b ->
                getLargest(b, 2)
            }
        )
    }

    override fun part2() {
        println(
            banks.sumOf { b ->
                getLargest(b, 12)
            }
        )
    }

    private fun getLargest(b: String, len: Int): Long {
        var res = ""
        var index = 0
        var max = 9
        while (res.length < len) {
            val i = b.indexOf(max.digitToChar(), index)
            if (i == - 1) {
                max--
                continue
            }
            if (i + (len - res.length - 1) >= b.length) {
                max--
                continue
            }
            res += max
            max = 9
            index = i + 1
        }
        return res.toLong()
    }

    private fun findLargestDigit(s: String, index: Int): Int {
        var max = s[index].digitToInt()
        var res = index
        for (i in (index + 1) until s.length) {
            if (s[i].digitToInt() > max) {
                max = s[i].digitToInt()
                res = i
            }
        }
        return res
    }

}