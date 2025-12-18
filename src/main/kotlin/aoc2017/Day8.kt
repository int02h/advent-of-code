package aoc2017

import AocDay2
import Input
import kotlin.math.max

class Day8 : AocDay2 {

    private lateinit var instructions: List<Instruction>
    private val regs = mutableMapOf<String, Int>().withDefault { 0 }

    override fun readInput(input: Input) {
        instructions = input.asLines().map { line ->
            val (regAndOp, cond) = line.split(" if ")
            val (reg, op, value) = regAndOp.split(" ")
            Instruction(reg, op, value.toInt(), Condition(cond))
        }
    }

    override fun part1() {
        instructions.forEach { it.execute() }
        println(regs.values.max())
    }

    override fun part2() {
        var max = Int.MIN_VALUE
        instructions.forEach {
            it.execute()
            if (regs.isNotEmpty()) {
                max = max(max, regs.values.max())
            }
        }
        println(max)
    }

    private inner class Condition(value: String) {
        private val op1: String
        private val op2: String
        private val check: String

        init {
            val parts = value.split(" ")
            op1 = parts[0]
            check = parts[1]
            op2 = parts[2]
        }

        fun isTrue(): Boolean {
            val op1 = op1.toIntOrNull() ?: regs.getValue(op1)
            val op2 = op2.toIntOrNull() ?: regs.getValue(op2)
            return when (check) {
                ">" -> op1 > op2
                "<" -> op1 < op2
                ">=" -> op1 >= op2
                "<=" -> op1 <= op2
                "==" -> op1 == op2
                "!=" -> op1 != op2
                else -> error("Unknown check: $check")
            }
        }
    }

    private inner class Instruction(val reg: String, val op: String, val value: Int, val cond: Condition) {
        fun execute() {
            if (cond.isTrue()) {
                when (op) {
                    "inc" -> regs[reg] = regs.getValue(reg) + value
                    "dec" -> regs[reg] = regs.getValue(reg) - value
                    else -> error("Unknown op: $op")
                }
            }
        }
    }

}