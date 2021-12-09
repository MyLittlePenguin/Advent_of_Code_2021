val input = java.io.File("09/input.txt").readLines().map { line -> line.map { it.toString().toInt() }.toTypedArray() }.toTypedArray()
//val input = java.io.File("09/test.txt").readLines().map { line -> line.map { it.toString().toInt() }.toTypedArray() }.toTypedArray()
data class Low(val row: Int, val col: Int)
val lowPoints = mutableListOf<Low>()

for(row in 0 until input.size) {
    for(col in 0 until input[row].size) {
        var up = if(row == 0) true else input[row][col] < input[row - 1][col]
        var down = if(row == input.size - 1) true else input[row][col] < input[row + 1][col]
        var left = if(col == 0) true else input[row][col] < input[row][col - 1]
        var right = if(col == input[0].size - 1) true else input[row][col] < input[row][col + 1]
        
        if(up && down && right && left) lowPoints.add(Low(row, col) )
    }
}
println(lowPoints.sumOf { input[it.row][it.col] + 1 })

fun checkBasin(seed: Low): Int {
    var (row, col) = seed
    val current = input[row][col]
    val checkField = { it: Int -> it >= 9 || current > it }
    input[row][col] = input[row][col] or 0b1000000
    
    return (if(row == 0 || checkField(input[row - 1][col])) 0 else 1 + checkBasin(Low(row - 1, col))) +
        (if(row == input.size - 1 || checkField(input[row + 1][col])) 0 else 1 + checkBasin(Low(row + 1, col))) +
        (if(col == input[row].size - 1 || checkField(input[row][col + 1])) 0 else 1 + checkBasin(Low(row, col + 1))) +
        (if(col == 0 || checkField(input[row][col - 1])) 0 else 1 + checkBasin(Low(row, col - 1)))
}

lowPoints.map { 1 + checkBasin(it) }.sortedDescending().let { println(it[0] * it[1] * it[2]) }