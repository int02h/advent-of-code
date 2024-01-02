package aoc2019

import AocDay
import Input

object Day8 : AocDay {
    private const val WIDTH = 25
    private const val HEIGHT = 6
    private const val LAYER_SIZE = WIDTH * HEIGHT

    override fun part1(input: Input) {
        val layers = readLayers(input)
        val minZeroLayer = layers.minBy { layer -> layer.count { it == '0' } }
        val oneDigitCount = minZeroLayer.count { it == '1' }
        val twoDigitCount = minZeroLayer.count { it == '2' }
        println(oneDigitCount * twoDigitCount)
    }

    override fun part2(input: Input) {
        val layers = readLayers(input)
        val image = Array(HEIGHT) { IntArray(WIDTH) { 2 } }
        layers.forEach { layer ->
            layer.forEachIndexed { index, pixel ->
                val row = index / WIDTH
                val col = index % WIDTH
                if (image[row][col] == 2) {
                    image[row][col] = pixel.digitToInt()
                }
            }
        }

        image.forEach { row ->
            row.forEach { pixel -> print(if (pixel == 1) '*' else ' ') }
            println()
        }
    }

    private fun readLayers(input: Input): List<String> {
        val data = input.asText()
        val layerCount = data.length / LAYER_SIZE
        return (0 until layerCount).map { index ->
            data.substring(index * LAYER_SIZE, (index + 1) * LAYER_SIZE)
        }
    }
}