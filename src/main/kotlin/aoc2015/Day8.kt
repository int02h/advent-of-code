package aoc2015

import Input

object Day8 {

    fun part1(input: Input) {
        var original = 0
        var decoded = 0
        input.asLines().forEach { line ->
            original += line.length
            decoded += line.decode().length
        }
        println(original - decoded)
    }

    fun part2(input: Input) {
        var original = 0
        var encoded = 0
        input.asLines().forEach { line ->
            original += line.length
            encoded += line.encode().length
        }
        println(encoded - original)
    }

    private fun String.decode(): String {
        val result = StringBuilder()
        var i = 1
        while (i < length - 1) {
            if (this[i] == '\\') {
                when {
                    this[i + 1] == '\\' -> {
                        result.append('\\')
                        i += 2
                    }
                    this[i + 1] == '"' -> {
                        result.append('"')
                        i += 2
                    }
                    else -> {
                        result.append("${this[i + 2]}${this[i + 3]}".toInt(16).toChar())
                        i += 4
                    }
                }
            } else {
                result.append(this[i])
                i += 1
            }
        }
        return result.toString()
    }

    private fun String.encode(): String {
        val result = StringBuilder()
        var i = 0
        while (i < length) {
            when (this[i]) {
                '\\' -> result.append("\\\\")
                '"' -> result.append("\\\"")
                else -> result.append(this[i])
            }
            i++
        }
        return "\"$result\""
    }

}