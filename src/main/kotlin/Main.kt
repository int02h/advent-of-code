import org.reflections.Reflections

fun main() {
    val year = 2015
    val reflections = Reflections("aoc${year}")
    val instance = reflections.getSubTypesOf(AocDay::class.java)
        .mapNotNull { it.kotlin.objectInstance }
        .sortedBy { it::class.simpleName }
        .last()
    instance.part1(Input(year, getDayIndex(instance)))
    instance.part2(Input(year, getDayIndex(instance)))
}

private fun getDayIndex(instance: AocDay): Int = instance::class.simpleName!!.takeLastWhile { it.isDigit() }.toInt()
