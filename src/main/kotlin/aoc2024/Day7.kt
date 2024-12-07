package aoc2024

import AocDay
import Input
import kotlin.math.pow
import kotlin.math.roundToInt

object Day7 : AocDay {

    override fun part1(input: Input) {
        val equations = readInput(input)
        println(
            equations.filter { eq -> eq.check() }
                .sumOf { eq -> eq.testValue }
        )
    }

    override fun part2(input: Input) {
        val equations = readInput(input)
        println(
            equations.filter { eq -> eq.check(isPart2 = true) }
                .sumOf { eq -> eq.testValue }
        )
    }

    private fun readInput(input: Input): List<Equation> =
        input.asLines().map { Equation.parse(it) }

    private class Equation(
        val testValue: Long,
        val operands: List<Long>
    ) {
        fun check(isPart2: Boolean = false): Boolean {
            val operations = IntArray(operands.size - 1)
            val count = (if (isPart2) 3.0 else 2.0).pow(operands.size - 1.0).roundToInt()
            repeat(count) {
                val result = calculate(operations)
                if (result == testValue) {
                    return true
                }
                next(operations, isPart2)
            }
            return false
        }

        private fun calculate(operations: IntArray): Long {
            var result = operands[0]
            for (index in 1..operands.lastIndex) {
                val operation = Operation.values()[operations[index - 1]]
                result = operation.invoke(result, operands[index])
            }
            return result
        }

        private fun next(operations: IntArray, isPart2: Boolean) {
            operations[0]++
            var index = 0
            val limit = if (isPart2) 3 else 2
            while (index < operations.lastIndex && operations[index] == limit) {
                operations[index] = 0
                operations[index + 1]++
                index++
            }
        }

        companion object {
            fun parse(value: String): Equation {
                val (testValue, operands) = value.split(": ")
                return Equation(
                    testValue = testValue.toLong(),
                    operands = operands.split(' ').map { it.toLong() }
                )
            }
        }
    }

    enum class Operation {
        ADD {
            override fun invoke(op1: Long, op2: Long): Long = op1 + op2
        },
        MUL {
            override fun invoke(op1: Long, op2: Long): Long = op1 * op2
        },
        CONCAT {
            override fun invoke(op1: Long, op2: Long): Long = "$op1$op2".toLong()
        };

        abstract fun invoke(op1: Long, op2: Long): Long
    }

}