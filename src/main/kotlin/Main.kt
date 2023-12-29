import org.reflections.Reflections

fun main() {
    val year = 2019
    val reflections = Reflections("aoc${year}")
    val instance = reflections.getSubTypesOf(AocDay::class.java)
        .mapNotNull { it.kotlin.objectInstance }
        .maxBy { it::class.simpleName!!.dropWhile { ch -> !ch.isDigit() }.toInt() }
    instance.part1(Input(year, getDayIndex(instance)))
    instance.part2(Input(year, getDayIndex(instance)))
}

private fun getDayIndex(instance: AocDay): Int = instance::class.simpleName!!.takeLastWhile { it.isDigit() }.toInt()
