package aoc2019

import AocDay
import Input
import util.MathUtil
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.sign

object Day12 : AocDay {

    override fun part1(input: Input) {
        val moons = parse(input)
        repeat(1000) {
            doStep(moons)
        }
        println(
            moons.sumOf { m -> m.potentialEnergy * m.kineticEnergy }
        )
    }

    override fun part2(input: Input) {
        val lengths = mutableListOf<Long>()
        repeat(4) { moonIndex ->
            val lenX = getRepeatLength(input, moonIndex) { it.x }
            val lenY = getRepeatLength(input, moonIndex) { it.y }
            val lenZ = getRepeatLength(input, moonIndex) { it.z }
            lengths += MathUtil.lcm(listOf(lenX, lenY, lenZ))
            println(lengths)
        }
        println(lengths.max())
    }

    private fun getRepeatLength(input: Input, moonIndex: Int, coordinate: (Moon) -> Int): Long {
        val moons = parse(input)
            val values = mutableListOf<Int>()
        do {
            doStep(moons)
            values += coordinate(moons[moonIndex])
        } while (!hasRepeats(values, 2))
        return values.size / 2L
    }

    private fun hasRepeats(values: List<Int>, count: Int): Boolean {
        if (values.size % count != 0) {
            return false
        }
        val length = values.size / count
        val pattern = values.take(length)
        for (i in 1 until count) {
            val sublist = values.subList(i * length, (i + 1) * length)
            if (sublist != pattern) {
                return false
            }
        }
        return true
    }

    private fun doStep(moons: List<Moon>) {
        for (i in moons.indices) {
            for (j in (i + 1)..moons.lastIndex) {
                val mi = moons[i]
                val mj = moons[j]

                val xGravity = (mj.x - mi.x).sign
                mi.vx += xGravity
                mj.vx -= xGravity

                val yGravity = (mj.y - mi.y).sign
                mi.vy += yGravity
                mj.vy -= yGravity

                val zGravity = (mj.z - mi.z).sign
                mi.vz += zGravity
                mj.vz -= zGravity
            }
        }
        moons.forEach { m ->
            m.x += m.vx
            m.y += m.vy
            m.z += m.vz
        }
    }

    private fun parse(input: Input): List<Moon> {
        val patter = Pattern.compile("x=([-0-9]+).*y=([-0-9]+).*z=([-0-9]+)")
        return input.asLines().map { line ->
            val matcher = patter.matcher(line)
            assert(matcher.find())
            Moon(matcher.group(1).toInt(), matcher.group(2).toInt(), matcher.group(3).toInt())
        }
    }

    private data class Moon(
        var x: Int,
        var y: Int,
        var z: Int,
        var vx: Int = 0,
        var vy: Int = 0,
        var vz: Int = 0,
    ) {
        val potentialEnergy: Int
            get() = abs(x) + abs(y) + abs(z)

        val kineticEnergy: Int
            get() = abs(vx) + abs(vy) + abs(vz)
    }
}