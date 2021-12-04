val input = java.io.File("04/input.txt").readLines().toMutableList()

data class BingoNum(val num: Int, var marked: Boolean = false)
data class Board(val fields: List<BingoNum>, var won: Boolean = false) {
    fun calcResult(winningNum: Int) = this.fields.map { if (it.marked) 0 else it.num }.sum().let { it * winningNum }
}

val nSeq = input.first().split(",").map { it.toInt() }
input.removeFirst()

var boards = input.windowed(6, 6).map {
    Board(it.slice(1..5).map {
        it.trim().split("  ", " ").map { BingoNum(it.toInt()) }
    }.flatten())
}

var result1 = -1
var result2 = -2

for (n in nSeq) {
    for (board in boards) {
        for (field in board.fields) {
            if (field.num == n) {
                field.marked = true
                break
            }
        }
        val indexedFields = board.fields.withIndex()
        if (indexedFields.groupBy { it.index % 5 }.values.any { col -> col.all { it.value.marked } }
            || indexedFields.groupBy { it.index / 5 }.values.any { row -> row.all { it.value.marked } }
        ) {
            board.won = true
            if(result1 == -1) result1 = board.calcResult(n)
            else result2 = board.calcResult(n)
        }
    }
    boards = boards.filter { !it.won }
    if (boards.size == 0) break
}

println(result1)
println(result2)