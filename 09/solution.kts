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
    if(input[row][col] >= 9) return -1
    
    var up = if(row == 0 || input[row - 1][col] >= 9 || input[row][col] > input[row - 1][col]) 0 else 1
    var down = if(row == input.size - 1 || input[row + 1][col] >= 9 || input[row][col] > input[row + 1][col]) 0 else 1
    var right = if(col == input[row].size - 1 || input[row][col + 1] >= 9 || input[row][col] > input[row][col + 1]) 0 else 1
    var left = if(col == 0 || input[row][col - 1] >= 9 || input[row][col] > input[row][col - 1]) 0 else 1
    
    input[row][col] = input[row][col] or 0b1000000
    if(up == 1 && row > 0) up += checkBasin(Low(row - 1, col))
    if(down == 1 && row < input.size - 1) down += checkBasin(Low(row + 1, col))
    if(left == 1 && col > 0) left += checkBasin(Low(row, col - 1))
    if(right == 1 && col < input[row].size - 1) right += checkBasin(Low(row, col + 1))
    return up + down + left + right
}

lowPoints.map { 1 + checkBasin(it) }.sortedDescending().let { println(it[0] * it[1] * it[2]) }