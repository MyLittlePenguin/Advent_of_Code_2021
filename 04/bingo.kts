val input = java.io.File("04/input.txt").readLines()

data class BingoNum(
    val num: Int,
    var marked: Boolean = false
)

data class Board(
    val fields: List<List<BingoNum>>,
    var won: Boolean = false,
    val rowNums: Array<Int> = Array<Int>(5) { 0 },
    val colNums: Array<Int> = Array<Int>(5) { 0 }
)

val iter = input.iterator()
val nSeq = iter.next().split(",").map { it.toInt() }
var boards: List<Board> = listOf()

do {
    iter.next()
    val board: MutableList<List<BingoNum>> = mutableListOf()
    repeat(5) {
        board.add(
            iter.next().trim().split("  ", " ").map { BingoNum(it.toInt()) }
        )
    }
    boards += listOf(Board(board))
} while (iter.hasNext())

var winningBoards: MutableList<Board> = mutableListOf()
var winningNums: MutableList<Int> = mutableListOf()

for (n in nSeq) {
    for (board in boards) {
        val fields = board.fields
        for (row in 0 until fields.size) {
            for (col in 0 until fields[row].size) {
                fields[row][col].let {
                    if(it.num == n) {
                        board.colNums[col] += 1
                        board.rowNums[row] += 1
                        it.marked = true
                    }
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
}

fun Board.calcResult(winningNum: Int) = this.fields.flatten().map { if(it.marked) 0 else it.num }.sum().let {
    it * winningNum
}

val result1 = winningBoards.first().calcResult(winningNums.first())
val result2 = winningBoards.last().calcResult(winningNums.last())

println(result1)
println(result2)

