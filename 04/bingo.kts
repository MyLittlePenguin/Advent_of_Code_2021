val input = java.io.File("04/input.txt").readLines()

data class BingoNum(
    val num: Int,
    var marked: Boolean = false
)

data class Board(
    val fields: Array<Array<BingoNum>>,
    var won: Boolean = false,
    val rowNums: Array<Int> = Array<Int>(5) { 0 },
    val colNums: Array<Int> = Array<Int>(5) { 0 }
)

val iter = input.iterator()
val nSeq = iter.next().split(",").map { it.toInt() }
var boards: List<Board> = listOf()

do {
    iter.next()
    var board: Array<Array<BingoNum>> = arrayOf()
    repeat(5) {
        board += iter.next().trim().split("  ", " ").map { BingoNum(it.toInt()) }.toTypedArray()
    }
    boards += listOf(Board(board))
} while (iter.hasNext())

var winningBoards: MutableList<Board> = mutableListOf()
var winningNums: MutableList<Int> = mutableListOf()

for (n in nSeq) {
    for (board in boards) {
        board.fields.forEachIndexed { rowNum, row ->
            row.forEachIndexed { colNum, col ->
                if(col.num == n) {
                    board.colNums[colNum] += 1
                    board.rowNums[rowNum] += 1
                    col.marked = true
                }
            }
        }
        if(board.colNums.contains(5) || board.rowNums.contains(5)) {
            winningBoards.add(board)
            winningNums.add(n)
            board.won = true
        }
    }
    boards = boards.filter { !it.won }
    if(boards.size == 0) break
}

fun Board.calcResult(winningNum: Int) =
    this.fields.flatten().map { if(it.marked) 0 else it.num }.sum().let { it * winningNum }

val result1 = winningBoards.first().calcResult(winningNums.first())
val result2 = winningBoards.last().calcResult(winningNums.last())

println(result1)
println(result2)