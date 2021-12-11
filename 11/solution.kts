import kotlin.math.*

//val input = java.io.File("11", "test.txt").readLines().map { it.map { it.toString().toInt() }.toTypedArray() }.toTypedArray()
val input = java.io.File("11", "input.txt").readLines().map { it.map { it.toString().toInt() }.toTypedArray() }.toTypedArray()

var flashCounter = 0
var stepCounter = 0
var firstOmniflash = 0

while(firstOmniflash == 0) {
    for(row in 0 until 10) {
        for(col in 0 until 10) {
            input[row][col]++
        }
    }
    for(row in 0 until 10) {
        for(col in 0 until 10) {
            if(input[row][col] > 9) flashOctopus(row, col)
        }
    }
    stepCounter++
    if(stepCounter == 100) println(flashCounter)
    if(firstOmniflash == 0 && input.flatten().count { it == 0 } == 100) {
        firstOmniflash = stepCounter
        println(firstOmniflash)
    }
}

fun flashOctopus(row: Int, col: Int) {
    input[row][col] = 0
    flashCounter++

    for(y in max(row - 1, 0) .. min(row + 1, 9)) {
        for(x in max(col - 1, 0) .. min(col + 1, 9)) {
            if(input[y][x] != 0) {
                input[y][x]++
                if(input[y][x] > 9) flashOctopus(y, x)
            }
        }
    }
}