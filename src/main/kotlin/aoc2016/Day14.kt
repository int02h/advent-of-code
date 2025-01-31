package aoc2016

import AocDay2
import Input
import util.Maze
import java.math.BigInteger
import java.security.MessageDigest
import java.util.SortedSet

class Day14 : AocDay2 {

    private lateinit var salt: String

    private val md = MessageDigest.getInstance("MD5")
    private val digest = ByteArray(16)

    override fun readInput(input: Input) {
        salt = input.asText().trim()
    }

    override fun part1() {
        findIndex { md5("$salt$it") }
    }

    override fun part2() {
        findIndex {
            var hash = md5("$salt$it")
            repeat(2016) { hash = md5(hash) }
            hash
        }
    }

    private fun findIndex(getHash: (Int) -> String) {
        var index = 0
        var keyLeft = 64
        val queue = mutableListOf<Pair<Int, String>>()

        while (keyLeft > 0) {
            val hash = getHash(index)

            while (queue.isNotEmpty() && queue[0].first < index) queue.removeFirst()

            val it = queue.iterator()
            while (it.hasNext()) {
                val (maxIndex, value) = it.next()
                if (index <= maxIndex) {
                    if (hash.contains(value)) {
                        keyLeft--
                        if (keyLeft == 0) {
                            println(maxIndex - 1000)
                            break
                        }
                        it.remove()
                    }
                }
            }

            for (i in 0 until (hash.length - 2)) {
                if (hash[i] == hash[i + 1] && hash[i + 1] == hash[i + 2]) {
                    queue += (index + 1000) to hash[i].toString().repeat(5)
                    break
                }
            }

            index++
        }
    }

    private fun md5(value: String): String {
        md.update(value.toByteArray())
        md.digest(digest, 0, digest.size)
        return String.format("%032x", BigInteger(1, digest))
    }
}

