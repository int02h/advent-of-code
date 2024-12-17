package aoc2024

import AocDay2
import Input

class Day17 : AocDay2 {

    private var regA: Long = 0
    private var regB: Long = 0
    private var regC: Long = 0

    private lateinit var program: IntArray
    private var ip: Int = 0

    private val output = mutableListOf<Int>()

    override fun readInput(input: Input) {
        val (a, b, c, _, p) = input.asLines()
        regA = a.drop(12).toLong()
        regB = b.drop(12).toLong()
        regC = c.drop(12).toLong()
        program = p.drop(9).split(',').map { it.toInt() }.toIntArray()
    }

    override fun part1() {
        execAll()
        println(output.joinToString(separator = ","))
    }

    override fun part2() {
        fun resetAndExec(a: Long) {
            regA = a
            regB = 0
            regC = 0
            ip = 0
            output.clear()
            execAll()
        }

        var count = 3
        var targetOutput = program.takeLast(count)

        var a = 0L
        while (true) {
            resetAndExec(a)
            if (output == targetOutput) {
                if (count == program.size) {
                    break
                }
                count++
                targetOutput = program.takeLast(count)
                a *= 8
            } else {
                a++
            }
        }
        println(a)
    }

    private fun execAll() {
        while (ip + 1 < program.size) {
            execNext()
        }
    }

    private fun execNext() {
        val opcode = program[ip++]
        when (opcode) {
            0 /* adv */ -> {
                val numerator = regA
                val denominator = 1.shl(getComboOperand().toInt())
                regA = numerator / denominator
            }
            1 /* bxl */ -> regB = regB xor getLiteral().toLong()
            2 /* bst */ -> regB = getComboOperand() % 8
            3 /* jnz */ -> if (regA != 0L) ip = getLiteral()
            4 /* bxc */ -> {
                regB = regB xor regC
                // For legacy reasons, this instruction reads an operand but ignores it
                getLiteral()
            }
            5 /* out */ -> output += (getComboOperand() % 8).toInt()
            6 /* bdv */ -> {
                val numerator = regA
                val denominator = 1.shl(getComboOperand().toInt())
                regB = numerator / denominator
            }
            7 /* cdv */ -> {
                val numerator = regA
                val denominator = 1.shl(getComboOperand().toInt())
                regC = numerator / denominator
            }
        }
    }

    private fun getLiteral(): Int = program[ip++]

    private fun getComboOperand(): Long {
        val value = program[ip++]
        return when (value) {
            in 0..3 -> value.toLong()
            4 -> regA
            5 -> regB
            6 -> regC
            else -> error("Invalid value: $value")
        }
    }

}