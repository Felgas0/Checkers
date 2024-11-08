package isel.leic.tds.checkers.model

const val BOARD_DIM = 4

@JvmInline
value class Column(val index: Int) {

    val symbol: Char get() = 'a' + index

    companion object {
        val values: List<Column> = List(BOARD_DIM) { Column(it) }
    }

    init {
        if(index < 0 || index >= BOARD_DIM) throw IllegalArgumentException("Invalid column index: $index")
    }
/*
    override fun equals(other: Any?):Boolean{
        if (other !is Column?) return false
        return this === other || (other is Column && index == other.index)
    }

    override fun hashCode(): Int = index * 31
*/
}

fun Char.toColumnOrNull(): Column?{
    return if (this in 'a'..< 'a' + BOARD_DIM) Column(this - 'a')
    else null
}
