package util

object MathUtil {
    // Greatest common divisor
    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    // Least common multiple
    fun lcm(a: Long, b: Long): Long {
        return (a * b) / gcd(a, b)
    }

    fun lcm(values: Collection<Long>): Long =
        values.fold(1L) { acc, v -> lcm(acc, v) }
}