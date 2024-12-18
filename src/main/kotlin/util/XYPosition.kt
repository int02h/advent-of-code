package util

import kotlin.math.abs

data class XYPosition(val x: Int, val y: Int) {
    val up: XYPosition get() = copy(y = y - 1)
    val down: XYPosition get() = copy(y = y + 1)
    val left: XYPosition get() = copy(x = x - 1)
    val right: XYPosition get() = copy(x = x + 1)

    fun distanceTo(other: XYPosition): Int = abs(other.x - this.x) + abs(other.y - this.y)
}