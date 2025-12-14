package aoc2017

import AocDay2
import Input

class Day4 : AocDay2 {

    private lateinit var phrases: List<String>

    override fun readInput(input: Input) {
        phrases = input.asLines()
    }

    override fun part1() {
        println(phrases.count(::isValid))
    }

    override fun part2() {
        println(phrases.count(::isValid2))
    }

    private fun isValid(phrase: String): Boolean {
        val words = phrase.split(" ")
        return words.size == words.toSet().size
    }

    private fun isValid2(phrase: String): Boolean {
        val words = phrase.split(" ")
        for (i in words.indices) {
            for (j in (i+1) until words.size) {
                val wi = words[i]
                val wj = words[j]
                if (wi.toSet() == wj.toSet()) {
                    return false
                }
            }
        }
        return true
    }

}