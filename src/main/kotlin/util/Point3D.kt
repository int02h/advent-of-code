package util

import kotlin.math.pow
import kotlin.math.sqrt

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(p: Point3D): Float {
        return sqrt((1.0f * x - p.x).pow(2) + (1.0f * y - p.y).pow(2) + (1.0f * z - p.z).pow(2))
    }
}