//val input = java.io.File("12", "test.txt").readLines().map { it.split("-") }
val input = java.io.File("12", "input.txt").readLines().map { it.split("-") }

fun checkPath2(connections: List<List<String>>, path: String = "", cave: String = "start", part2: Boolean = false): Int {
    return if (cave == "end") 1
    else if (cave == "start" && path.contains(cave)) 0
    else if (cave.lowercase() == cave && path.contains(cave)
        && (!part2 || path.split("/").filter { it.lowercase() == it }.groupingBy { it }.eachCount().values.contains(2))) 0
    else connections.map {
        when (cave) {
            it[0] -> checkPath2(connections, "$path/$cave", it[1], part2)
            it[1] -> checkPath2(connections, "$path/$cave", it[0], part2)
            else -> 0
        }
    }.sum()
}

println(checkPath2(input))
println(checkPath2(input, part2 = true))