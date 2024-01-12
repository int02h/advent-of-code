import org.reflections.Reflections

fun main() {
    val year = 2019
    val reflections = Reflections("aoc${year}")
    val instance = reflections.getSubTypesOf(AocDay::class.java)
        .mapNotNull { it.kotlin.objectInstance }
        .maxBy { it::class.simpleName!!.dropWhile { ch -> !ch.isDigit() }.toInt() }

    var start = System.currentTimeMillis()
    instance.part1(Input(year, getDayIndex(instance)))
    println("Part 1 took ${System.currentTimeMillis() - start} ms")

    start = System.currentTimeMillis()
    instance.part2(Input(year, getDayIndex(instance)))
    println("Part 2 took ${System.currentTimeMillis() - start} ms")
}

private fun getDayIndex(instance: AocDay): Int = instance::class.simpleName!!.takeLastWhile { it.isDigit() }.toInt()
