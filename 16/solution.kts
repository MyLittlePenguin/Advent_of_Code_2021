import java.lang.Exception

//var input = java.io.File("16", "test.txt").readText()
var input = java.io.File("16", "input.txt").readText()

interface Packet {
    val version: Int
    val pType: Int
    fun calc(): Long
    fun sumVersions(): Int
}

data class LiteralPacket(
    override val version: Int,
    override val pType: Int,
    var value: Long
) : Packet {
    override fun sumVersions() = this.version
    override fun calc() = this.value
}

data class OperatorPacket(
    override val version: Int,
    override val pType: Int,
    val packets: MutableList<Packet> = mutableListOf()
) : Packet {
    override fun sumVersions() = this.packets.sumOf { it.sumVersions() } + this.version
    override fun calc(): Long {
        return when(pType) {
            0 -> this.packets.map { it.calc() }.sumOf { it }
            1 -> this.packets.map { it.calc() }.reduce { acc, l -> acc * l }
            2 -> this.packets.minOf { it.calc() }
            3 -> this.packets.maxOf { it.calc() }
            5 -> this.packets.map { it.calc() }.reduce { acc, i -> if(acc > i) 1 else 0 }
            6 -> this.packets.map { it.calc() }.reduce { acc, i -> if(acc < i) 1 else 0 }
            7 -> this.packets.map { it.calc() }.reduce { acc, i -> if(acc == i) 1 else 0 }
            else -> throw Exception("kein gültiges OperatorPacket")
        }
    }
}

fun Int.isLastNumber() = this and 0b10000 != 0b10000
fun Int.getNumber() = this and 0b01111
fun Int.withoutOffset(offset: Int) = (if (offset > 0) this shr offset else this) and 255

class Parser(private val iterator: CharIterator) {
    private var current = 0
    private var offset = 0
    private var halfByteCounter = 0

    fun nextHalfByte(): Int {
        halfByteCounter++
        return this.iterator.next().toString().toInt(16)
    }

    fun getNextLiteral(): Pair<Int, Int> {
        current = 255 and when {
            offset >= 4 -> {
                offset -= 4
                current
            }
            offset > 0 -> {
                current.shl(4) or nextHalfByte()
            }
            offset < 0 -> {
                throw Exception("Offset $offset kleiner 0")
            }
            else -> {
                offset = 4
                nextHalfByte().shl(4) or nextHalfByte()
            }
        }
        return Pair(current, offset)
    }

    fun parsePacket(): Packet {
        // TODO: offset und lastBytes berücksichtigen
        getNextLiteral()
        //    var offset = oldOffset
        //    var current = lastBytes.shl(4) or iterator.nextHalfByte()
        val version = (current and 0b111.shl(++offset)) shr offset

        current = (current shl 4) or nextHalfByte()
        val pType = (current and 0b111.shl(++offset)) shr offset
        return if (pType == 4) {
            parseLiteralPacket(version, pType)
        } else {
            parseOperatorPacket(version, pType)
        }
    }

    fun parseLiteralPacket(version: Int, pType: Int): LiteralPacket {
        val oldOffset = offset
        getNextLiteral()
        if (oldOffset == 4) {
            current = current.shl(4) or nextHalfByte()
            offset = 4
        }
        var withoutOffset = current.withoutOffset(--offset)

        var value = 0L
        while (!withoutOffset.isLastNumber()) {
            value = value.shl(4) or withoutOffset.getNumber().toLong()
            getNextLiteral()
            withoutOffset = current.withoutOffset(--offset)
        }
        value = value.shl(4) or withoutOffset.getNumber().toLong()

        return LiteralPacket(version, pType, value)
    }

    fun parseOperatorPacket(version: Int, pType: Int): OperatorPacket {
        offset--
        getNextLiteral()
        offset += 4
        var withoutOffset = current.withoutOffset(offset--)
        val lType = withoutOffset and 1
        var length = 0
        var bitCounter = 1
        val lengthBits = if (lType == 1) 11 else 15
        while (bitCounter < lengthBits) {
            getNextLiteral()
            withoutOffset = current.withoutOffset(offset)

            if (bitCounter + 4 <= lengthBits) {
                length = length.shl(4) or withoutOffset.getNumber()
                bitCounter += 4
            } else {
                val delta = lengthBits - bitCounter
                offset += delta
                length = length.shl(delta) or current.withoutOffset(offset)
                bitCounter += delta
            }
        }
        var subPackets = mutableListOf<Packet>()
        if (lType == 1) {
            repeat(length) {
                subPackets.add(parsePacket())
            }
        } else {
            val oldPos = halfByteCounter * 4 - offset
            while (halfByteCounter * 4 - offset - oldPos < length) {
                subPackets.add(parsePacket())
            }
        }

        return OperatorPacket(version, pType, subPackets)
    }
}

if (input.length % 2 == 1) input += "0"

val packet = Parser(input.iterator()).parsePacket()
println(packet.sumVersions())
println(packet.calc())