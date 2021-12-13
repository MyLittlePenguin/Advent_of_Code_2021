import kotlin.math.max

//val input = java.io.File("13", "test.txt").readLines()
val input = java.io.File("13", "input.txt").readLines()
data class Coordinate(val x: Int, val y: Int)

val coordinates = input.filter { it.length > 0 && !it.startsWith("fold") }
    .map { it.split(",").map { it.toInt() } }.map { Coordinate(it[0], it[1]) }
val folds = input.filter { it.startsWith("fold") }.map { it.split("=") }
val paperSize = coordinates.reduce { acc, it -> Coordinate(max(it.x, acc.x), max(it.y, acc.y)) }

var paper = Array(paperSize.x + 1) { Array(paperSize.y + 1) { false } }
coordinates.forEach { paper[it.x][it.y] = true}

fun foldX(paper: Array<Array<Boolean>>, foldX: Int): Array<Array<Boolean>> {
    var folded = Array(paper.size) { Array(paper.first().size) { false } }
    for (x in 1..foldX) {
        for (y in 0 until paper[x].size) {
            folded[foldX - x][y] = paper[foldX - x][y] || paper[foldX + x][y]
        }
    }
    return folded.slice(0 until foldX).toTypedArray()
}

fun foldY(paper: Array<Array<Boolean>>, foldY: Int): Array<Array<Boolean>> {
    var folded = Array(paper.size) { Array(paper.first().size) { false } }
    for (y in 1..foldY) {
        for (x in 0 until paper.size) {
            folded[x][foldY - y] = paper[x][foldY - y] || paper[x][foldY + y]
        }
    }
    return folded.map { it.slice(0 until foldY).toTypedArray() }.toTypedArray()
}

fun fold(it: List<String>) = if(it[0].endsWith("y")) foldY(paper, it[1].toInt()) else foldX(paper, it[1].toInt())
fun Array<Array<Boolean>>.print() = Array(this.first().size) { y -> Array(this.size) { x -> this[x][y] } }
    .forEach { it.map { if(it) '#' else '.' }.joinToString("").let { println(it) } }

println(fold(folds.first()).flatten().count { it })
folds.forEach { paper = fold(it) }
paper.print()