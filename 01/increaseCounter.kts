fun List<Int>.incCounter() = this.windowed(2, 1) { if(it[0] < it[1]) 1 else 0 }.sum()

val input = java.io.File("01/input.txt").readLines(Charsets.UTF_8).map { it.toInt() }

println(input.incCounter())

println(
    input.windowed(3, 1) { it.sum() }.incCounter()
)