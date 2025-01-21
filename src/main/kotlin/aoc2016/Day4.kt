package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day4 : AocDay2 {

    private lateinit var rooms: List<Room>

    override fun readInput(input: Input) {
        rooms = input.asLines().map { line ->
            val sectorId = line.findAllNumbers()[0]
            val index = line.indexOf("-$sectorId")
            Room(
                name = line.substring(0, index),
                sectorId = sectorId,
                checkSum = line.substring(index + "-$sectorId".length + 1, line.lastIndex)
            )
        }
    }

    override fun part1() {
        println(rooms.filter { it.isReal() }.sumOf { it.sectorId })
    }

    override fun part2() {
        rooms.filter { it.isReal() }
            .forEach { println("${it.sectorId} ${it.decodeName()}") }
    }

    private data class Room(
        val name: String,
        val sectorId: Int,
        val checkSum: String
    ) {
        fun isReal(): Boolean {
            val map = mutableMapOf<Char, Int>()
            name.forEach { ch -> map[ch] = map.getOrDefault(ch, 0) + 1 }
            map.remove('-')
            val checkSum = map.entries
                .sortedWith { e1, e2 -> if (e1.value == e2.value) e1.key.compareTo(e2.key) else e2.value - e1.value }
                .take(5)
                .map { it.key }
                .joinToString("")
            return checkSum == this.checkSum
        }

        fun decodeName(): String {
            val alphabet = "abcdefghijklmnopqrstuvwxyz"
            return name.map { ch ->
                if (ch == '-') {
                    ' '
                } else {
                    var index = alphabet.indexOf(ch)
                    index = (index + sectorId) % alphabet.length
                    alphabet[index]
                }
            }.joinToString("")
        }
    }
}