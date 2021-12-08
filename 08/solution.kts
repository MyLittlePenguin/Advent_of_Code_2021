//val input = java.io.File("08/test.txt").readLines().map { display ->
val input = java.io.File("08/input.txt").readLines().map { display ->
    display.split(" | ", " ").map { it.toSortedSet().joinToString("") }
}

input.flatMap { it.slice(10..13) }.groupingBy { it.length }.eachCount()
    .entries.sumOf { when (it.key) { 2, 3, 4, 7 -> it.value; else -> 0 } }
    .let { println(it) }

var sum = 0
for (display in input) {
    val map = HashMap<Int, String>()
    map[1] = display.first { it.length == 2 }
    map[7] = display.first { it.length == 3 }
    map[4] = display.first { it.length == 4 }
    map[8] = display.first { it.length == 7 }

    display.forEach {
        val knownSegments = it.count { segment -> map[7]!!.contains(segment) || map[4]!!.contains(segment) }
        val notInSegmentsOfSeven = it.count { segment -> !map[7]!!.contains(segment) }
        when {
            knownSegments == 3 && it.length == 5 -> map[2] = it
            knownSegments == 5 && it.length == 6 -> map[9] = it
            knownSegments == 4 && it.length == 6 && notInSegmentsOfSeven == 4 -> map[6] = it
            knownSegments == 4 && it.length == 6 && notInSegmentsOfSeven == 3 -> map[0] = it
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 2 -> map[3] = it
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 3 -> map[5] = it
        }
    }

    display.slice(10..13).map {
        var result = 0
        for ((digit, segments) in map) {
            if (segments == it) {
                result = digit
                break
            }
        }
        result
    }.reduce { acc, i -> acc * 10 + i }.let { sum += it }
}

println(sum)