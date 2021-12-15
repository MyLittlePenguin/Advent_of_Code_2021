data class Knoten(
    val value: Int,
    val neighbors: MutableList<Knoten> = mutableListOf<Knoten>(),
    var predecessor: Knoten? = null,
    var dist: Int = Int.MAX_VALUE
)

//val input = java.io.File("15", "test.txt").readLines().map { it.map { Knoten(it.digitToInt()) } }
val input = java.io.File("15", "input.txt").readLines().map { it.map { Knoten(it.digitToInt()) } }

//val input2 = java.io.File("15", "test.txt").readLines().map { line ->
val input2 = java.io.File("15", "input.txt").readLines().map { line ->
    Array(line.length * 5) { line[it % line.length].digitToInt() + (it / line.length) }.map { if (it <= 9) it else it - 9 }
}.toMutableList()

val tmp = input2.slice(0..input2.size - 1)
for(i in 1 .. 4) {
    tmp.forEach { line ->
        input2.add(line.map { it + i }.map { if(it <= 9) it else it - 9 })
    }
}

fun connect(input: List<List<Knoten>>) {
    input.forEachIndexed { rowNum, row ->
        row.forEachIndexed { colNum, col ->
            if (rowNum - 1 >= 0) col.neighbors.add(input[rowNum - 1][colNum])
            if (rowNum + 1 <= input.size - 1) col.neighbors.add(input[rowNum + 1][colNum])
            if (colNum - 1 >= 0) col.neighbors.add(input[rowNum][colNum - 1])
            if (colNum + 1 <= input.first().size - 1) col.neighbors.add(input[rowNum][colNum + 1])
        }
    }
}

fun updateKnoten(current: Knoten, known: Knoten) {
    val newDist = current.value + known.dist
    if (newDist > 0 && newDist < current.dist) {
        current.dist = newDist
        current.predecessor = known
    }
}

fun solve(field: List<List<Knoten>>) {
    field.first().first().dist = 0
    val knoten = field.flatten()
    var solution = 0
    while(solution != knoten.last().dist) {
        solution = knoten.last().dist
        knoten.forEach { current ->
            current.neighbors.forEach { updateKnoten(it, current) }
        }
    }
    println(knoten.last().dist)
}

connect(input)
solve(input)

val part2 = input2.map { line -> line.map { Knoten(it) } }
connect(part2)
solve(part2)