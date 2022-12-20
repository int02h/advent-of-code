package aoc2022

import Input
import java.util.Stack

object Day18 {

    fun part1(input: Input) {
        val field = mutableSetOf<Cube>()
        val cubes = input.asLines()
            .map { line -> line.split(",").map { it.toInt() } }
            .map { (x, y, z) -> Cube(x, y, z) }

        cubes.forEach { cube -> field.add(cube) }

        println(cubes.sumOf { cube -> 6 - cube.countNeighbors(field) })
    }

    fun part2(input: Input) {
        val field = mutableSetOf<Cube>()
        val cubes = input.asLines()
            .map { line -> line.split(",").map { it.toInt() } }
            .map { (x, y, z) -> Cube(x, y, z) }

        cubes.forEach { cube -> field.add(cube) }

        val minX = cubes.minOf { it.x } - 1
        val maxX = cubes.maxOf { it.x } + 1
        val minY = cubes.minOf { it.y } - 1
        val maxY = cubes.maxOf { it.y } + 1
        val minZ = cubes.minOf { it.z } - 1
        val maxZ = cubes.maxOf { it.z } + 1

        val fillingField = field.toMutableSet()
        val stack = Stack<Cube>()
        stack.push(Cube(minX, minY, minZ))
        while (stack.isNotEmpty()) {
            val cube = stack.pop()
            if (fillingField.contains(cube)) {
                continue
            }
            fillingField.add(cube)

            if ((cube.x + 1) in minX..maxX) {
                stack.push(cube.copy(x = cube.x + 1))
            }
            if ((cube.x - 1) in minX..maxX) {
                stack.push(cube.copy(x = cube.x - 1))
            }
            if ((cube.y + 1) in minY..maxY) {
                stack.push(cube.copy(y = cube.y + 1))
            }
            if ((cube.y - 1) in minY..maxY) {
                stack.push(cube.copy(y = cube.y - 1))
            }
            if ((cube.z + 1) in minZ..maxZ) {
                stack.push(cube.copy(z = cube.z + 1))
            }
            if ((cube.z - 1) in minZ..maxZ) {
                stack.push(cube.copy(z = cube.z - 1))
            }
        }

        val emptyCubes = mutableListOf<Cube>()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val cube = Cube(x, y, z)
                    if (!fillingField.contains(cube)) {
                        emptyCubes += cube
                    }
                }
            }
        }
        emptyCubes.forEach { cube -> field.add(cube) }

        println(cubes.sumOf { cube -> 6 - cube.countNeighbors(field) })
    }

    private data class Cube(val x: Int, val y: Int, val z: Int) {

        fun countNeighbors(field: Set<Cube>): Int {
            val list = listOf(
                field.contains(Cube(x + 1, y, z)),
                field.contains(Cube(x - 1, y, z)),
                field.contains(Cube(x, y + 1, z)),
                field.contains(Cube(x, y - 1, z)),
                field.contains(Cube(x, y, z + 1)),
                field.contains(Cube(x, y, z - 1)),
            )
            return list.count { it }
        }
    }
}