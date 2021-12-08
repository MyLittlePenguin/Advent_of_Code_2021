//val input = java.io.File("08/test.txt").readLines().map { it.split(" | ", " " ) }
val input = java.io.File("08/input.txt").readLines().map { it.split(" | ", " ") }

input.flatMap { it.slice(10..13) }.groupingBy { it.length }.eachCount()
    .entries.filter { when(it.key) { 2 , 3, 4, 7 -> true ; else -> false } }.sumOf { it.value }
    .let { println(it) }

val numbers = mutableListOf<Int>()
for(display in input) {
    val one = display.first { it.length == 2 }.toSortedSet().joinToString("")
    val seven = display.first { it.length == 3 }.toSortedSet().joinToString("")
    val four = display.first { it.length == 4 }.toSortedSet().joinToString("")
    val eight = display.first { it.length == 7 }.toSortedSet().joinToString("")
    
    var two = ""
    var three = ""
    var five = ""
    var six = ""
    var nine = ""
    
    fun isInOneSevenFour(char: Char) = one.contains(char) || seven.contains(char) || four.contains(char)
    display.forEach {
        val knownSegments = it.count { segment -> isInOneSevenFour(segment)}
        val notInSegmentsOfSeven = it.count { segment -> !seven.contains(segment)}
        when {
            knownSegments == 3 && it.length == 5 -> two = it.toSortedSet().joinToString("")
            knownSegments == 5 && it.length == 6 -> nine = it.toSortedSet().joinToString("")
            knownSegments == 4 && it.length == 6 && notInSegmentsOfSeven == 4 -> six = it.toSortedSet().joinToString("")
    
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 2 -> three = it.toSortedSet().joinToString("")
            knownSegments == 4 && it.length == 5 && notInSegmentsOfSeven == 3 -> five = it.toSortedSet().joinToString("")
        }
    }
    //2 -> 3 bekannte 2 unbekannte
    // 3 -> 4 bekannte 1 unbekannte
    // 5 -> 4 bekannte 1 unbekannte
    // 6 -> 4 bekannte 2 unbekannte 4 nicht mit 7 gemeinsam
    // 9 -> 5 bekannte 1 unbekannte
    display.slice(10 .. 13).map { when(it.toSortedSet().joinToString("")) {
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