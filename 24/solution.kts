//val testInput = java.io.File("24", "test.txt").readLines()
val input = java.io.File("24", "input.txt").readLines()

class ALU {
    private val memory = mutableMapOf<String, Long>()
    private var program = mutableListOf<() -> Unit>()
    private var input = ""
    private var inputPointer = 0

    private fun initMemory() {
        memory.clear()
        memory["w"] = 0L
        memory["x"] = 0L
        memory["y"] = 0L
        memory["z"] = 0L
    }
    private fun inp(): Long = input[inputPointer++].digitToInt().toLong()
    private fun parseB(b: String): Long = memory[b] ?: b.toLong()

    fun parse(instructions: String) = parse(instructions.lines())
    fun parse(instructions: List<String>) {
        program.clear()
        instructions.forEach { instruction ->
            val op = instruction.split(" ")
            val parsedOp: () -> Unit = when(op.first()) {
                "inp" -> { ->
                    println(memory)
                    memory[op[1]] = inp()
                }
                "add" -> { -> memory[op[1]] = memory[op[1]]!! + parseB(op[2]) }
                "mul" -> { -> memory[op[1]] = memory[op[1]]!! * parseB(op[2]) }
                "div" -> { -> memory[op[1]] = memory[op[1]]!! / parseB(op[2]) }
                "mod" -> { -> memory[op[1]] = memory[op[1]]!! % parseB(op[2]) }
                "eql" -> { -> memory[op[1]] = if(memory[op[1]]!! == parseB(op[2])) 1L else 0L }
                else -> { -> }
            }
            program.add(parsedOp)
        }
    }

    fun exec(input: String): List<Pair<String, Long>> {
        initMemory()
        this.inputPointer = 0
        this.input = input
        program.forEach { it() }
        return memory.entries.map { Pair(it.key, it.value) }
    }
}

val alu = ALU()
//alu.exec("91599994399395").let { println(it) }

fun calc(input: String): Long {
    var pointer = 0
    var w = input[pointer++].digitToInt().toLong()
    // 1.
    var z = w + 5L
    // 2.
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 14L // w muss 1 sein
    // 3.
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 15L // w muss 5 sein
    // 4.
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 16L // w = 9
    // 5
    w = input[pointer++].digitToInt().toLong()
    var x = if((z % 26L - 16L) != w) 1L else 0L // bezug zu w4
    z = z / 26L * (25L * x + 1L) + ((w + 8L) * x) // x muss 0 sein w darf alles sein solang es das gleiche wie w4 ist
    // 6
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 11L != w) 1L else 0L // bezug zu w3
    z = z / 26L * (25L * x + 1L) + ((w + 9L) * x) // x muss 0 sein w darf alles sein (vergl w3
    // 7
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 6L != w) 1L else 0L // bezug zu w2
    z = z / 26L * (25L * x + 1L) + ((w + 2L) * x) // x muss 0 sein w darf alles sein (vergl w2
    // 8
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 13L // w muss 4 sein
    // 9
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 16L // w muss 3 sein
    // 10
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 10L != w) 1L else 0L // bezug zu 9
    z = z / 26 * (25L * x + 1L) + ((w + 6L) * x) // x muss 0 sein damit kann w beliebig sein w = 9
    // 11
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 8L != w) 1L else 0L // bezug zu 8
    z = z / 26L * (25L * x + 1L) + ((w + 6L) * x) // x muss 0 sein damit kann w beliebig sein w = 9
    // 12
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 11L != w) 1L else 0L // bezug zu w1
    z = z / 26L * (25L * x + 1L) + ((w + 9L) * x) // x muss 0 sein damit kann w beliebig sein // z muss 0 werden
    // 13
    w = input[pointer++].digitToInt().toLong()
    z = z * 26L + w + 11L // w = 9 // z
    // 14
    w = input[pointer++].digitToInt().toLong()
    x = if(z % 26L - 15L != w) 1L else 0L // w = 5 begzug zu w13
    z = z / 26L * (25L * x + 1L) + ((w + 5L) * x)
    return z
}

// Part 1
// damit z am ende 0 ist muss x = 0 sein -> $14 muss 4 kleiner sein als $13
// -> $14 = 5
// -> $13 = 9
// -> $12 = 3
// -> $11 = 9
// -> $10 = 9
// ->  $9 = 3
// ->  $8 = 4
// ->  $7 = 9
// ->  $6 = 9
// ->  $5 = 9
// ->  $4 = 9
// ->  $3 = 5
// ->  $2 = 1
// ->  $1 = 9

//91599994399995
println(
    calc("91599994399395")
)


// Part 2
// $14 = 1
// $13 = 5
// $12 = 1
// $11 = 6
// $10 = 7
// $09 = 1
// $08 = 1
// $07 = 9
// $06 = 5
// $05 = 1
// $04 = 1
// $03 = 1
// $02 = 1
// $01 = 8
println(
    calc("71111591176151")
)