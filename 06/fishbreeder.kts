data class Fish(var state: Long, var count: Long)

val input = java.io.File("06", "input.txt").readText().trim().split(",").groupingBy { it.toInt() }.eachCount()
var fishswarm = input.entries.map { Fish(it.key.toLong(), it.value.toLong()) }

repeat(256) {
    fishswarm.forEach { it.state-- }
    fishswarm += fishswarm.filter { it.state == -1L }.map { it.state = 6; Fish(8, it.count) }
    fishswarm = fishswarm.groupBy { it.state }.values.map {
        it.reduce { acc, fish ->
            Fish(acc.state, acc.count + fish.count)
        }
    }
}
println(fishswarm.sumOf { it.count })