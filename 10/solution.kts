//val input = java.io.File("10", "test.txt").readLines()
val input = java.io.File("10", "input.txt").readLines()

data class Line(val code: String, val pos: Int, val score: Int)

var tabCount = 0
fun parseChunk(code: String): Line {
    var pos = 0
    var score: Int
    var codeOut = ""
    do {
        val res = parseChunk(code , pos)
        pos = res.pos
        score = res.score
        codeOut += res.code
    }
    while(score == 0 && ++pos < code.length)
    return Line(codeOut, pos, score)
}
fun parseChunk(code: String, begin: Int): Line {
    val closingMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    var pos = begin
    val closing = closingMap[code[pos]]
    var errorScore = 0
    var codeOut = ""
    var chunkEnd = false
    while(errorScore == 0 && !chunkEnd && pos < code.length - 1) {
        when(code[++pos]) {
            '(', '[', '{', '<' -> {
                val (newCode, newPos, newErrorScore) = parseChunk(code, pos)
                pos = newPos
                errorScore = newErrorScore
                codeOut = newCode
            }
            else -> {
                errorScore = when(code[pos]) {
                    closing -> 0
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> -1
                }
                chunkEnd = true
            }
        }
    }
    return Line(if(pos == code.length - 1 && !chunkEnd) codeOut + closing else codeOut, pos, errorScore)
}

val pointMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
val parsed = input.map { it to parseChunk(it) }
println(parsed.sumOf { it.second.score })
parsed.map { it.second.code }.filter { it != "" }
    .map { it.map { pointMap[it]!!.toLong() }.reduce { acc, i -> acc * 5 + i } }
    .sorted().let { println(it[it.size/2]) }