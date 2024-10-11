package isel.leic.tds.checkers.model

import java.util.Stack

class Board(val grid: MutableMap<Square, Char?>, val turn: Char) {

    init {
        // Initialize BLACK pieces
        Square.values.forEach { square ->
            if (square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black) {
                grid[square] = 'B'
            }
        }

        // Initialize WHITE pieces
        Square.values.forEach { square ->
            if (square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black) {
                grid[square] = 'W'
            }
        }
    }

    fun Board.canPlay(startPos:String, endPos:String): Boolean {
        val sp = startPos.toSquareOrNull()
        val ep = endPos.toSquareOrNull()
        if (sp == null || ep == null) return false  // if the positions are not squares cant play
        val piece = grid[sp] ?: return false        // if the selected square by the user does not have any piece can't play
        if (piece != turn) return false             // if the piece selected by a player is an opponent piece can't play
        if (grid[ep] != null) return false          // if there is a piece on the end position can't play
        if (anyPieceCanCapture()) return canCapture(sp) //checks if the move is to capture a piece if he can capture any

        TODO()

    }

    fun Board.isWinner(p: Char): Boolean = grid.values.none(){it != p}

    private fun anyPieceCanCapture(): Boolean = grid.filter { it.value == turn }.keys.any() { canCapture(it)}

    private fun canCapture(startingSquare: Square):Boolean {
        val stack = Stack<Square>()
        val moves = listOf(
            Pair(2, 2),
            Pair(2, -2),
            Pair(-2, 2),
            Pair(-2, -2)
        )

        stack.push(startingSquare)
        while (stack.isNotEmpty()){
            val current = stack.pop()
            for (move in moves){
                /*
                val newRow = current.row.index + 2 + move.first
                val newCol = Column(current.column.index + 2)
                val midRow = Row(current.row.index + 1)
                val midCol = Column(current.column.index + 1)
                 */
                val newSquare = Square(Row(current.row.index + move.first), Column(current.column.index + move.second))
                val midSquare = Square(Row(current.row.index + move.first / 2), Column(current.column.index + move.second / 2))

                if (newSquare in Square.values){
                    if (grid[midSquare] != null && grid[midSquare] != turn && grid[newSquare] == null) {
                        stack.push(newSquare)
                        return true
                    }
                }
            }
        }
        return false
    }
}
