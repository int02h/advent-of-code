package util

operator fun List<String>.get(p: Maze.Point): Char = this[p.y][p.x]