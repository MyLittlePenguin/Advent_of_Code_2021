val input = java.io.File("04/input.txt").readLines()

data class BingoNum(val num: Int, var marked: Boolean = false)
data class Board(val fields: Array<BingoNum>, var won: Boolean = false) {
    fun calcResult(winningNum: Int) = this.fields.map { if (it.marked) 0 else it.num }.sum().let { it * winningNum }
}

val iter = input.iterator()
val nSeq = iter.next().split(",").map { it.toInt() }
var boards: List<Board> = listOf()

do {
    iter.next()
    var board: Array<BingoNum> = arrayOf()
    repeat(5) {
        board += iter.next().trim().split("  ", " ").map { BingoNum(it.toInt()) }
    }
    boards += Board(board)
} while (iter.hasNext())

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