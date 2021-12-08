//val input = java.io.File("08/test.txt").readLines().map { display ->
val input = java.io.File("08/input.txt").readLines().map { display ->
    display.split(" | ", " ").map { it.toSortedSet().joinToString("") }
}

input.flatMap { it.slice(10..13) }.groupingBy { it.length }.eachCount()
    .entries.filter { when(it.key) { 2 , 3, 4, 7 -> true ; else -> false } }.sumOf { it.value }
    .let { println(it) }

val numbers = mutableListOf<Int>()
for(display in input) {
    val one = display.first { it.length == 2 }
    val seven = display.first { it.length == 3 }
    val four = display.first { it.length == 4 }
    val eight = display.first { it.length == 7 }
    
    var two = ""
    var three = ""
    var five = ""
    var six = ""
    var nine = ""
    
    display.forEach {
        val knownSegments = it.count { segment -> seven.contains(segment) || four.contains(segment)}
        val notInSegmentsOfSeven = it.count { segment -> !seven.contains(segment)}
        when {
            knownSegments == 3 && it.length == 5 -> two = it
            knownSegments == 5 && it.length == 6 -> nine = it
            knownSegments == 4 && it.length == 6 && notInSegmentsOfSeven == 4 -> six = it
    
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 2 -> three = it
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 3 -> five = it
        }
    }
    
    display.slice(10 .. 13).map { when(it) {
        one -> "1"
        two -> "2"
        three -> "3"
        four -> "4"
        five -> "5"
        six -> "6"
        seven -> "7"
        eight -> "8"
        nine -> "9"
        else -> "0"
    } }.joinToString("").toInt().let { numbers.add(it) }
}

println(numbers.sum())