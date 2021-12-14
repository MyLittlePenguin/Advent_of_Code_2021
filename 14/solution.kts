//val input = java.io.File("14", "test.txt").readLines().toMutableList()
val input = java.io.File("14", "input.txt").readLines().toMutableList()

var polymer = input.removeFirst()
input.removeFirst()

val map = HashMap<String, String>()
input.map { it.split(" -> ") }.forEach { map[it[0]] = it[1] }

var charCount = mutableMapOf<Char, Long>()
polymer.groupingBy { it }.eachCount().entries.forEach { (key, value) -> charCount[key] = value.toLong() }

var polymerGrouping = mutableMapOf<String, Long>()
polymer.windowed(2, 1).groupingBy { it }.eachCount().entries.forEach { (key, value) -> polymerGrouping[key] = value.toLong() }

fun build() {
    var newGrouping = mutableMapOf<String, Long>()
    polymerGrouping.entries.forEach { (key, value) ->
        val newChar = map[key]!!
        newGrouping[key[0] + newChar] = (newGrouping[key[0] + newChar] ?: 0) + value
        newGrouping[newChar + key[1]] = (newGrouping[newChar + key[1]] ?: 0) + value
        charCount[newChar.first()] = (charCount[newChar.first()] ?: 0) + value
    }
    polymerGrouping = newGrouping
}

repeat(10) { build() }
println(charCount.values.sorted().let { it.last() - it.first() })

repeat(30) { build() }
println(charCount.values.sorted().let { it.last() - it.first() })