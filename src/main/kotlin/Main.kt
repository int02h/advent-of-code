import org.reflections.Reflections

fun main() {
    val year = 2017
    val reflections = Reflections("aoc${year}")
    val cls = reflections.getSubTypesOf(AocDay2::class.java)
        .maxBy { it.simpleName.dropWhile { ch -> !ch.isDigit() }.toInt() }

    var instance = cls.getDeclaredConstructor().newInstance()
    var start = System.currentTimeMillis()
    instance.readInput(Input(year, getDayIndex(instance)))
    instance.part1()
    println("Part 1 took ${System.currentTimeMillis() - start} ms")

    instance = cls.getDeclaredConstructor().newInstance()
    start = System.currentTimeMillis()
    instance.readInput(Input(year, getDayIndex(instance)))
    instance.part2()
    println("Part 2 took ${System.currentTimeMillis() - start} ms")
}

private fun getDayIndex(instance: AocDay2): Int = instance::class.simpleName!!.takeLastWhile { it.isDigit() }.toInt()
