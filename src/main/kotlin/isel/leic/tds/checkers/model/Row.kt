package isel.leic.tds.checkers.model

//Linha 1 = BOARD_DIM - 1
//Linha 8 = BOARD_DIM - (BOARD_DIM - 1)

@JvmInline
value class Row (val index: Int) {

    val digit: Char get() = (BOARD_DIM - index).digitToChar()  // index = BOARD_DIM  - digit

    companion object {
        val values: List<Row> = List(BOARD_DIM) { Row(it) }

    }

    init {
        require(index in 0..< BOARD_DIM) { "Invalid row index: $index" }
    }

    /*   override fun equals(other: Any?): Boolean {
        if (other !is Row?) return false
        return this === other || (other is Row && index == other.index)
    }

    override fun hashCode(): Int = index * 31

}

  */
}

    fun Char.toRowOrNull(): Row? {
        val r = BOARD_DIM.digitToChar() - this
        return if (r in 0 until BOARD_DIM) Row(r) else null
    }
