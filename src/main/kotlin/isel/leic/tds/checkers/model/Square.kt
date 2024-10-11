package isel.leic.tds.checkers.model

const val BOARD = BOARD_DIM * BOARD_DIM

class Square private constructor(val index: Int) {

    val row get() = Row(index / BOARD_DIM)
    val column get() = Column(index % BOARD_DIM)

    init {
        if (index < 0 || index >= BOARD) {
            throw IllegalArgumentException("Invalid Square index: $index")
        }
    }

    companion object{
        val values :List<Square> = List(BOARD) { Square(it)}
    }

    val black: Boolean get() = (row.index + column.index) % 2 != 0
    //@black se a soma de ambos for ímpar entao é quadrado preto

    override fun toString() = "${row.digit}${column.symbol}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Square) return false
        return row == other.row && column == other.column
    }

    override fun hashCode():Int = (row.hashCode() + column.hashCode()) * 31

    fun isDiagonal(to: Square?): Boolean {
        return to != null && row.index - to.row.index == column.index - to.column.index
    }
}

fun String.toSquareOrNull(): Square? {
    return Square(
        this[0].toRowOrNull() ?: return null,
        this[1].toColumnOrNull() ?: return null
    )
}

fun String.toSquare(): Square = toSquareOrNull() ?: throw IllegalArgumentException("Invalid Square Format")

fun Square(row: Row, col: Column): Square = Square.values[row.index * BOARD_DIM + col.index % BOARD_DIM]


fun main(){
    println(Square.values)
}