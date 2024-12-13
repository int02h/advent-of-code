package aoc2024

import AocDay
import Input
import java.util.Stack
import util.RowColPosition as Position

object Day12 : AocDay {

    override fun part1(input: Input) {
        val map = input.asLines()
        val visited = mutableSetOf<Position>()

        fun getPerimeter(region: List<Position>): Int {
            if (region.isEmpty()) {
                return 0
            }
            val type = map.get(region.first())
            return region.sumOf { p ->
                var res = 0
                if (map.get(p.up) != type) res++
                if (map.get(p.right) != type) res++
                if (map.get(p.down) != type) res++
                if (map.get(p.left) != type) res++
                res
            }
        }

        var result = 0
        for (row in map.indices) {
            for (col in map[0].indices) {
                val region = findRegion(row, col, map, visited)
                val area = region.size
                val perimeter = getPerimeter(region)
                result += area * perimeter
            }
        }
        println(result)
    }

    override fun part2(input: Input) {
        val map = input.asLines()
        val visited = mutableSetOf<Position>()

        fun getSideCount(region: List<Position>): Int {
            if (region.isEmpty()) {
                return 0
            }
            val minRow = region.minOf { it.row }
            val minCol = region.minOf { it.col }
            val maxRow = region.maxOf { it.row }
            val maxCol = region.maxOf { it.col }
            var sideCount = 0

            for (r in minRow..maxRow) {
                var isUpRow = false
                var isDownRow = false
                for (c in minCol..maxCol) {
                    val p = Position(r, c)
                    if (!region.contains(p)) {
                        isUpRow = false
                        isDownRow = false
                        continue
                    }
                    if (!region.contains(p.up)) {
                        if (!isUpRow) {
                            isUpRow = true
                            sideCount++
                        }
                    } else {
                        isUpRow = false
                    }
                    if (!region.contains(p.down)) {
                        if (!isDownRow) {
                            isDownRow = true
                            sideCount++
                        }
                    } else {
                        isDownRow = false
                    }
                }
            }

            for (c in minCol..maxCol) {
                var isLeftRow = false
                var isRightRow = false
                for (r in minRow..maxRow) {
                    val p = Position(r, c)
                    if (!region.contains(p)) {
                        isLeftRow = false
                        isRightRow = false
                        continue
                    }
                    if (!region.contains(p.left)) {
                        if (!isLeftRow) {
                            isLeftRow = true
                            sideCount++
                        }
                    } else {
                        isLeftRow = false
                    }
                    if (!region.contains(p.right)) {
                        if (!isRightRow) {
                            isRightRow = true
                            sideCount++
                        }
                    } else {
                        isRightRow = false
                    }
                }
            }

            return sideCount
        }

        var result = 0
        for (row in map.indices) {
            for (col in map[0].indices) {
                val region = findRegion(row, col, map, visited)
                val area = region.size
                val sideCount = getSideCount(region)
                result += area * sideCount
            }
        }
        println(result)
    }

    private fun findRegion(row: Int, col: Int, map: List<String>, visited: MutableSet<Position>): List<Position> {
        if (visited.contains(Position(row, col))) {
            return emptyList()
        }
        val stack = Stack<Position>()
        stack.push(Position(row, col))
        val type = map[row][col]
        val region = mutableSetOf<Position>()

        while (stack.isNotEmpty()) {
            val position = stack.pop()
            region += position
            visited += position
            position.up.let { p -> if (map.get(p) == type && !visited.contains(p)) stack.push(p) }
            position.right.let { p -> if (map.get(p) == type && !visited.contains(p)) stack.push(p) }
            position.down.let { p -> if (map.get(p) == type && !visited.contains(p)) stack.push(p) }
            position.left.let { p -> if (map.get(p) == type && !visited.contains(p)) stack.push(p) }
        }

        return region.toList()
    }

    private fun List<String>.get(position: Position): Char {
        return getOrNull(position.row)?.getOrNull(position.col) ?: '0'
    }

}