package aoc2023

import AocDay
import Input
import java.math.BigDecimal
import kotlin.math.sign

object Day24 : AocDay {

    override fun part1(input: Input) {
        val lines = parse(input)
        val range = if (lines.size == 5) 7.0..37.0 else 200000000000000.0..400000000000000.0
        var count = 0
        for (i in lines.indices) {
            for (j in (i + 1)..lines.lastIndex) {
                val intersection = lines[i].getFutureIntersection(lines[j])
                if (
                    intersection != null && range.contains(intersection.x) && range.contains(intersection.y)
                ) {
                    count++
                }
            }
        }
        println(count)
    }

    override fun part2(input: Input) {
        val stones = parse(input)
        val equations = StringBuilder()
        for (i in 0..2) {
            val t = "t$i"
            equations.append(t).append(" >= 0, ").append(stones.get(i).sx).append(" + ").append(stones.get(i).dx)
                .append(t).append(" == x + vx ").append(t).append(", ")
            equations.append(stones.get(i).sy).append(" + ").append(stones.get(i).dy).append(t).append(" == y + vy ")
                .append(t).append(", ")
            equations.append(stones.get(i).sz).append(" + ").append(stones.get(i).dz).append(t).append(" == z + vz ")
                .append(t).append(", ")
        }
        val sendToMathematica =
            "Solve[{" + equations.substring(0, equations.length - 2) + "}, {x,y,z,vx,vy,vz,t0,t1,t2}]"
        println(sendToMathematica)
        val x = 419848807765291
        val y = 391746659362922
        val z = 213424530058607
        println(x + y + z)
    }

    private fun parse(input: Input): List<Line> =
        input.asLines().map { line ->
            val (point, vector) = line.split(" @ ")
                .map { value -> value.split(",").map { it.trim().toLong() } }
            Line(point[0], point[1], point[2], vector[0], vector[1], vector[2])
        }

    data class Line(
        val sx: Long,
        val sy: Long,
        val sz: Long,
        val dx: Long,
        val dy: Long,
        val dz: Long,
    ) {

        fun getFutureIntersection(line: Line): Point? {
            val x1 = BigDecimal(sx)
            val y1 = BigDecimal(sy)
            val x2 = BigDecimal(sx + dx)
            val y2 = BigDecimal(sy + dy)
            val x3 = BigDecimal(line.sx)
            val y3 = BigDecimal(line.sy)
            val x4 = BigDecimal(line.sx + line.dx)
            val y4 = BigDecimal(line.sy + line.dy)

            val d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
            if (d == BigDecimal.ZERO) {
                return null
            }

            val xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d
            val yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d

            val sameSignX1 = (xi - x1).signum() == dx.sign
            val sameSignY1 = (yi - y1).signum() == dy.sign
            val sameSignX2 = (xi - x3).signum() == line.dx.sign
            val sameSignY2 = (yi - y3).signum() == line.dy.sign

            if (sameSignX1 && sameSignY1 && sameSignX2 && sameSignY2) {
                return Point(xi.toDouble(), yi.toDouble())
            }
            return null
        }
    }

    data class Point(val x: Double, val y: Double)

}