package aoc2022

import Input

object Day22 {

    fun part1(input: Input) {
        handle(input, PositionWrapperPart1())
    }

    fun part2(input: Input) {
        handle(input, PositionWrapperPart2())
    }

    private fun parseInput(input: Input): Pair<Map<Position, Char>, List<PathItem>> {
        val (mapInput, pathInput) = input.asText().split("\n\n")

        var y = 1
        val map = mutableMapOf<Position, Char>()
        mapInput.split('\n').forEach { mapRow ->
            var x = 1
            mapRow.forEach { ch ->
                if (ch != ' ') {
                    map[Position(x, y)] = ch
                }
                x++
            }
            y++
        }

        val numberBuilder = StringBuilder()
        val path = mutableListOf<PathItem>()
        pathInput.forEach { ch ->
            if (ch.isDigit()) {
                numberBuilder.append(ch)
            } else {
                if (numberBuilder.isNotEmpty()) {
                    path += PathItem.Move(numberBuilder.toString().toInt())
                    numberBuilder.clear()
                }
                path += PathItem.Rotate(ch)
            }
        }

        if (numberBuilder.isNotEmpty()) {
            path += PathItem.Move(numberBuilder.toString().toInt())
        }

        return map to path
    }

    private fun handle(input: Input, positionWrapper: PositionWrapper) {
        val (map, path) = parseInput(input)

        val startY = map.minOf { it.key.y }
        val startX = map.filter { it.key.y == startY }.minOf { it.key.x }
        val player = Player(startX, startY, map, positionWrapper)

        path.forEach { player.handle(it) }

        println(player.calculatePassword())
    }

    private data class Position(val x: Int, val y: Int)

    private sealed class PathItem {
        class Move(val tileCount: Int) : PathItem()
        class Rotate(val direction: Char) : PathItem()
    }

    private class Player(
        var x: Int,
        var y: Int,
        val map: Map<Position, Char>,
        val positionWrapper: PositionWrapper,
    ) {

        var direction: Direction = Direction.RIGHT

        fun handle(pathItem: PathItem) {
            when (pathItem) {
                is PathItem.Move -> {
                    for (i in 0 until pathItem.tileCount) {
                        if (x == 51 && y == 1 && this.direction == Direction.RIGHT) {
                            Unit
                        }
                        val (pos, direction) = positionWrapper.getWrappedPosition(map, x, y, direction)
                        if (!map.contains(pos)) {
                            Unit
                        }
                        when (map.getValue(pos)) {
                            '.' -> {
                                x = pos.x
                                y = pos.y
                            }
                            '#' -> break
                        }
                        this.direction = direction
                    }
                }
                is PathItem.Rotate -> {
                    direction = when (pathItem.direction) {
                        'R' -> direction.clockwise()
                        'L' -> direction.counterclockwise()
                        else -> error(pathItem.direction)
                    }
                }
            }
        }

        fun calculatePassword(): Int = y * 1000 + 4 * x + direction.ordinal
    }

    private enum class Direction(val dx: Int, val dy: Int) {
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0),
        UP(0, -1);

        fun clockwise(): Direction = values()[(ordinal + 1) % values().size]

        fun counterclockwise(): Direction {
            var index = ordinal - 1
            while (index < 0) index += values().size
            return values()[index % values().size]
        }
    }

    private interface PositionWrapper {
        fun getWrappedPosition(
            map: Map<Position, Char>,
            x: Int,
            y: Int,
            direction: Direction
        ): Pair<Position, Direction>
    }

    private class PositionWrapperPart1 : PositionWrapper {

        override fun getWrappedPosition(
            map: Map<Position, Char>,
            x: Int,
            y: Int,
            direction: Direction
        ): Pair<Position, Direction> {
            val ch = map[Position(x + direction.dx, y + direction.dy)]
            if (ch != null) {
                return Position(x + direction.dx, y + direction.dy) to direction
            }

            val dx = -direction.dx
            val dy = -direction.dy
            var wx = x
            var wy = y
            while (map.contains(Position(wx + dx, wy + dy))) {
                wx += dx
                wy += dy
            }
            return Position(wx, wy) to direction
        }

    }

    private class PositionWrapperPart2 : PositionWrapper {

        override fun getWrappedPosition(
            map: Map<Position, Char>,
            x: Int,
            y: Int,
            direction: Direction
        ): Pair<Position, Direction> {
            val side = CubeSide.values().first { x in it.xRange && y in it.yRange }
            return side.getWrappedPosition(map, x, y, direction)
        }

    }

    private enum class CubeSide(val xRange: IntRange, val yRange: IntRange) {
        A(101..150, 1..50) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(B.xRange.last, B.globalY(localY)) to Direction.LEFT
                }
                if (localX >= 50) {
                    return Position(D.xRange.last, D.globalY(localY.inverted())) to Direction.LEFT
                }
                if (localY < 0) {
                    return Position(F.globalX(localX), F.yRange.last) to Direction.UP
                }
                if (localY >= 50) {
                    return Position(C.xRange.last, C.globalY(localX)) to Direction.LEFT
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        },
        B(51..100, 1..50) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(E.xRange.first, E.globalY(localY.inverted())) to Direction.RIGHT
                }
                if (localX >= 50) {
                    return Position(A.xRange.first, A.globalY(localY)) to Direction.RIGHT
                }
                if (localY < 0) {
                    return Position(F.xRange.first, F.globalY(localX)) to Direction.RIGHT
                }
                if (localY >= 50) {
                    return Position(C.globalX(localX), C.yRange.first) to Direction.DOWN
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        },
        C(51..100, 51..100) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(E.globalX(localY), E.yRange.first) to Direction.DOWN
                }
                if (localX >= 50) {
                    return Position(A.globalX(localY), A.yRange.last) to Direction.UP
                }
                if (localY < 0) {
                    return Position(B.globalX(localX), B.yRange.last) to Direction.UP
                }
                if (localY >= 50) {
                    return Position(D.globalX(localX), D.yRange.first) to Direction.DOWN
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        },
        D(51..100, 101..150) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(E.xRange.last, E.globalY(localY)) to Direction.LEFT
                }
                if (localX >= 50) {
                    return Position(A.xRange.last, A.globalY(localY.inverted())) to Direction.LEFT
                }
                if (localY < 0) {
                    return Position(C.globalX(localX), C.yRange.last) to Direction.UP
                }
                if (localY >= 50) {
                    return Position(F.xRange.last, F.globalY(localX)) to Direction.LEFT
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        },
        E(1..50, 101..150) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(B.xRange.first, B.globalY(localY.inverted())) to Direction.RIGHT
                }
                if (localX >= 50) {
                    return Position(D.xRange.first, D.globalY(localY)) to Direction.RIGHT
                }
                if (localY < 0) {
                    return Position(C.xRange.first, C.globalY(localX)) to Direction.RIGHT
                }
                if (localY >= 50) {
                    return Position(F.globalX(localX), F.yRange.first) to Direction.DOWN
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        },
        F(1..50, 151..200) {
            override fun getWrappedPosition(
                map: Map<Position, Char>,
                x: Int,
                y: Int,
                direction: Direction
            ): Pair<Position, Direction> {
                val localX = (x + direction.dx).localX()
                val localY = (y + direction.dy).localY()
                if (localX < 0) {
                    return Position(B.globalX(localY), B.yRange.first) to Direction.DOWN
                }
                if (localX >= 50) {
                    return Position(D.globalX(localY), D.yRange.last) to Direction.UP
                }
                if (localY < 0) {
                    return Position(E.globalX(localX), E.yRange.last) to Direction.UP
                }
                if (localY >= 50) {
                    return Position(A.globalX(localX), A.yRange.first) to Direction.DOWN
                }
                return Position(x + direction.dx, y + direction.dy) to direction
            }
        };

        abstract fun getWrappedPosition(
            map: Map<Position, Char>,
            x: Int,
            y: Int,
            direction: Direction
        ): Pair<Position, Direction>

        protected fun globalX(localX: Int) = xRange.first + localX
        protected fun globalY(localY: Int) = yRange.first + localY

        protected fun Int.localX() = this - xRange.first
        protected fun Int.localY() = this - yRange.first

        protected fun Int.inverted() = 50 - this - 1
    }

}