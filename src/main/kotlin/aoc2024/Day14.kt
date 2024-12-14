package aoc2024

import AocDay2
import Input
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.regex.Pattern
import javax.imageio.ImageIO

class Day14 : AocDay2 {

    private lateinit var robots: List<Robot>

    private var q1 = 0
    private var q2 = 0
    private var q3 = 0
    private var q4 = 0

    override fun readInput(input: Input) {
        val pattern = Pattern.compile("(-?\\d+)")
        robots = input.asLines()
            .map { pattern.matcher(it) }
            .map { it.results().toList() }
            .map { result -> result.map { it.group().toInt() } }
            .map { Robot(it[0], it[1], it[2], it[3]) }
    }

    override fun part1() {
        repeat(100) {
            robots.forEach { it.move() }
        }
        calcQuadrants()
        println(q1 * q2 * q3 * q4)
    }

    override fun part2() {
        fun dump(index: Int) {
            val bmp = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
            robots.forEach { bmp.setRGB(it.x, it.y, Color.GREEN.rgb) }
            File("./robots").mkdirs()
            ImageIO.write(bmp, "png", File("./robots/second-$index.png"))
        }

        repeat(7600) {
            robots.forEach { r -> r.move() }
            if (it > 7400) {
                dump(it + 1)
            }
        }
    }

    private fun calcQuadrants() {
        q1 = 0
        q2 = 0
        q3 = 0
        q4 = 0
        robots.forEach {
            if (it.x < WIDTH / 2 && it.y < HEIGHT / 2) q1++
            if (it.x > WIDTH / 2 && it.y < HEIGHT / 2) q2++
            if (it.x < WIDTH / 2 && it.y > HEIGHT / 2) q3++
            if (it.x > WIDTH / 2 && it.y > HEIGHT / 2) q4++
        }
    }

    private data class Robot(
        var x: Int,
        var y: Int,
        val vx: Int,
        val vy: Int,
    ) {
        fun move() {
            x += vx
            if (x < 0) x += WIDTH
            if (x >= WIDTH) x -= WIDTH

            y += vy
            if (y < 0) y += HEIGHT
            if (y >= HEIGHT) y -= HEIGHT
        }
    }

    companion object {
        private const val WIDTH = 101
        private const val HEIGHT = 103
    }
}