package aoc2022

import Input

object Day23 {

    fun part1(input: Input) {
        var map = readMap(input)
        repeat(10) {
            map = doRound(map)
        }
        println(countEmptySpace(map))
    }

    fun part2(input: Input) {
        var map = readMap(input)
        var count = 0
        while (map.isNotEmpty()) {
            map = doRound(map)
            count++
        }
        println(count)
    }

    private fun doRound(map: Map<Position, Elf>): MutableMap<Position, Elf> {
        val count = mutableMapOf<Position, Int>()
        map.forEach { (pos, elf) ->
            val proposal = elf.makeProposal(pos, map)
            if (proposal != null) {
                count[proposal] = count.getOrDefault(proposal, 0) + 1
            }
        }

        if (count.isEmpty()) {
            return mutableMapOf()
        }

        val nextMap = mutableMapOf<Position, Elf>()
        map.forEach { (pos, elf) ->
            val proposal = elf.proposal
            if (proposal != null && count[proposal] == 1) {
                nextMap[proposal] = elf
            } else {
                nextMap[pos] = elf
            }
            elf.onRoundFinish()
        }
        return nextMap
    }

    private fun countEmptySpace(map: Map<Position, Elf>): Int {
        val minX = map.minOf { it.key.x }
        val maxX = map.maxOf { it.key.x }
        val minY = map.minOf { it.key.y }
        val maxY = map.maxOf { it.key.y }

        var count = 0
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (!map.contains(Position(x, y))) {
                    count++
                }
            }
        }
        return count
    }

    private fun readMap(input: Input): Map<Position, Elf> {
        val map = mutableMapOf<Position, Elf>()
        var y = 0
        input.asLines().forEach { line ->
            var x = 0
            line.forEach { ch ->
                if (ch == '#') {
                    map[Position(x, y)] = Elf()
                }
                x++
            }
            y++
        }
        return map
    }

    private fun dump(map: Map<Position, Elf>) {
        val minX = map.minOf { it.key.x }
        val maxX = map.maxOf { it.key.x }
        val minY = map.minOf { it.key.y }
        val maxY = map.maxOf { it.key.y }

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(if (map.contains(Position(x, y))) '#' else '.')
            }
            println()
        }
        println()
    }

    private data class Position(val x: Int, val y: Int) {
        val n: Position
            get() = Position(x, y - 1)
        val ne: Position
            get() = Position(x + 1, y - 1)
        val nw: Position
            get() = Position(x - 1, y - 1)
        val s: Position
            get() = Position(x, y + 1)
        val se: Position
            get() = Position(x + 1, y + 1)
        val sw: Position
            get() = Position(x - 1, y + 1)
        val w: Position
            get() = Position(x - 1, y)
        val e: Position
            get() = Position(x + 1, y)
    }

    private class Elf {

        val directions = Direction.values().toMutableList()
        var proposal: Position? = null

        fun makeProposal(pos: Position, map: Map<Position, Elf>): Position? {
            val needToMove = listOf(pos.n, pos.ne, pos.e, pos.se, pos.s, pos.sw, pos.w, pos.nw).any { map.contains(it) }
            proposal = if (needToMove) {
                val proposedDirection = directions.find { d -> d.getPositionToCheck(pos).all { !map.contains(it) } }
                proposedDirection?.handle(pos)
            } else {
                null
            }
            return proposal
        }

        fun onRoundFinish() {
            directions.add(directions.removeAt(0))
        }

    }

    private enum class Direction {
        NORTH {
            override fun getPositionToCheck(position: Position): List<Position> =
                listOf(position.n, position.ne, position.nw)

            override fun handle(pos: Position): Position = pos.n
        },
        SOUTH {
            override fun getPositionToCheck(position: Position): List<Position> =
                listOf(position.s, position.se, position.sw)

            override fun handle(pos: Position): Position = pos.s
        },
        WEST {
            override fun getPositionToCheck(position: Position): List<Position> =
                listOf(position.w, position.nw, position.sw)

            override fun handle(pos: Position): Position = pos.w
        },
        EAST {
            override fun getPositionToCheck(position: Position): List<Position> =
                listOf(position.e, position.ne, position.se)

            override fun handle(pos: Position): Position = pos.e
        };

        abstract fun getPositionToCheck(position: Position): List<Position>
        abstract fun handle(pos: Position): Position
    }
}