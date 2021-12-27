//val input = java.io.File("25", "test.txt").readLines()
val input = java.io.File("25", "input.txt").readLines()

fun Array<CharArray>.print() = this.forEach { println(it.joinToString("")) }

var field = input.map { line -> line.toCharArray() }.toTypedArray()
field.print()
println()
var tmp = field
var changed = false
var counter = 0
do {
    changed = false
    field = tmp
    tmp = Array<CharArray>(field.size) { CharArray(field.first().size) { '.' } }
    for(row in 0 until field.size) {
        for (col in 0 until field[row].size) {
            if(field[row][col] == '>') {
                val nextCol = if(col + 1 == field[row].size) 0 else col + 1
                if(field[row][nextCol] == '.') {
                    tmp[row][nextCol] = '>'
                    tmp[row][col] = '.'
                    changed = true
                }
                else tmp[row][col] = field[row][col]
            }
            else if(field[row][col] != '.') {
                tmp[row][col] = field[row][col]
            }
        }
    }

    field = tmp
    tmp = Array<CharArray>(field.size) { CharArray(field.first().size) { '.' } }
    for(row in 0 until field.size) {
        for (col in 0 until field[row].size) {
            if(field[row][col] == 'v') {
                val nextRow = if(row + 1 == field.size) 0 else row + 1
//                println("$row -> $nextRow")
                if(field[nextRow][col] == '.') {
                    tmp[nextRow][col] = 'v'
                    tmp[row][col] = '.'
                    changed = true
                }
                else tmp[row][col] = 'v'
            }
            else if(field[row][col] != '.') {
                tmp[row][col] = field[row][col]
            }
        }
    }
    println("runde ${++counter}")
    tmp.print()
    println()
//    Thread.sleep(1000)
} while(changed)

