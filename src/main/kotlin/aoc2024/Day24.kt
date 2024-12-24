package aoc2024

import AocDay2
import Input
import util.getBit
import java.io.File

class Day24 : AocDay2 {

    private val wires = mutableMapOf<String, Boolean>()
    private val gates = mutableListOf<Gate>()

    override fun readInput(input: Input) {
        val (values, connections) = input.asText().split("\n\n")
        values.split("\n").forEach { v ->
            val (name, initialValue) = v.split(": ")
            wires[name] = initialValue == "1"
        }
        connections.split("\n").forEach { conn ->
            val (in1, op, in2, _, out) = conn.split(" ")
            gates += Gate(in1, in2, op, out)
        }
    }

    override fun part1() {
        val allGates = gates.flatMap { listOf(it.in1, it.in2, it.out) }.toSet()
        while (wires.size < allGates.size) {
            gates.forEach { g -> g.exec(wires) }
        }
        println(wiresToNumber("z"))
    }

    override fun part2() {
        val swapped = mutableListOf<String>()
        fun swap(out1: String, out2: String) {
            val g1 = gates.filter { it.out == out1 }.also { if (it.size != 1) error(it) }.first()
            val g2 = gates.filter { it.out == out2 }.also { if (it.size != 1) error(it) }.first()
            g1.out = out2
            g2.out = out1
            swapped += out1
            swapped += out2
        }
        swap("djg", "z12")
        swap("sbg", "z19")
        swap("mcq", "hjm")
        swap("z37", "dsd")

        println(swapped.sorted().joinToString(","))

        val allGates = gates.flatMap { listOf(it.in1, it.in2, it.out) }.toSet()
        while (wires.size < allGates.size) {
            gates.forEach { g -> g.exec(wires) }
        }

        val x = wiresToNumber("x")
        val y = wiresToNumber("y")
        val sum = x + y
        val z = wiresToNumber("z")

        val wrongOutputs = mutableListOf<Int>()
        val zMax = wires.keys.filter { it.startsWith("z") }.maxOf { it.drop(1).toInt() }
        for (i in 0..zMax) {
            if (z.getBit(i) != sum.getBit(i)) {
                wrongOutputs += i
            }
        }

        buildString {
            append("strict digraph Aoc {\n")
            gates.forEach { g ->
                append("  ${g.in1} -> ${g.out} [label=\"${g.op}\"];\n")
                append("  ${g.in2} -> ${g.out} [label=\"${g.op}\"];\n")
            }
            for (i in 0..zMax) {
                if (wrongOutputs.contains(i)) {
                    append("  z${i.toString().padStart(2, '0')} [color=\"red\"];\n")
                } else {
                    append("  z${i.toString().padStart(2, '0')} [color=\"green\"];\n")
                }
            }
            append("}\n")
        }.also { File("output/aoc.dot").writeText(it) }

        if (wrongOutputs.isNotEmpty()) {
            error("Wrong connection")
        }
    }

    private fun wiresToNumber(prefix: String): Long {
        val wires = wires.keys.filter { it.startsWith(prefix) }
        var result = 0L
        wires.forEach { zw ->
            val bitIndex = zw.drop(1).toInt()
            if (this.wires.getValue(zw)) {
                result += 1L.shl(bitIndex)
            }
        }
        return result
    }

    private class Gate(val in1: String, val in2: String, val op: String, var out: String) {

        fun exec(wires: MutableMap<String, Boolean>) {
            val value1 = wires[in1] ?: return
            val value2 = wires[in2] ?: return
            val result = when (op) {
                "AND" -> value1 and value2
                "OR" -> value1 or value2
                "XOR" -> value1 xor value2
                else -> error(op)
            }
            wires[out] = result
        }
    }

}