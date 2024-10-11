package isel.leic.tds.checkers.model

import java.util.Stack

class Board(val grid: MutableMap<Square, Char?>, val turn: Char) {

    init {
        if (grid.isEmpty()) {
            // Initialize BLACK pieces
            Square.values.forEach { square ->
                if (square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black) {
                    grid[square] = 'b'
                }
            }

            // Initialize WHITE pieces
            Square.values.forEach { square ->
                if (square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black) {
                    grid[square] = 'w'
                }
            }
        }
    }

    fun play(startPos: String, endPos: String): Board {
        val sp = startPos.toSquare()
        val ep = endPos.toSquare()
        grid[ep] = grid[sp]
        grid[sp] = null

        if (grid[ep] == 'w' && ep.row.index == 0) {
            grid[ep] = 'W'
        } else if (grid[ep] == 'b' && ep.row.index == BOARD_DIM - 1) {
            grid[ep] = 'B'
        }
        return Board(grid, if (turn == 'w') 'b' else 'w')
    }

    fun Board.canPlay(startPos: String, endPos: String): Boolean {
        val sp = startPos.toSquareOrNull()
        val ep = endPos.toSquareOrNull()
        return when {
            sp == null || ep == null -> false  // if the positions are not squares can't play
            grid[sp] == null -> false          // if the selected square by the user does not have any piece can't play
            grid[sp] != turn -> false          // if the piece selected by a player is an opponent piece can't play
            grid[ep] != null -> false          // if there is a piece on the end position can't play
            //anyPieceCanCapture() -> canCapture(sp) // checks if the move is to capture a piece if he can capture any
            else -> true
        }
    }

    fun Board.isWinner(p: Char): Boolean = grid.values.none(){it != p}

    //private fun anyPieceCanCapture(): Boolean = grid.filter { it.value == turn }.keys.any() { canCapture(it)}

    /*private fun canCapture(startingSquare: Square): Boolean {
        val moves = listOf(
            Pair(2, 2),
            Pair(2, -2),
            Pair(-2, 2),
            Pair(-2, -2)
        )

        for (move in moves) {
            val newRow = startingSquare.row.index + move.first
            val newCol = startingSquare.column.index + move.second
            val midRow = startingSquare.row.index + move.first / 2
            val midCol = startingSquare.column.index + move.second / 2

            if (newRow in 0 until BOARD_DIM && newCol in 0 until BOARD_DIM) {
                val newSquare = Square(Row(newRow), Column(newCol))
                val midSquare = Square(Row(midRow), Column(midCol))

                if (grid[midSquare] != null && grid[midSquare] != turn && grid[newSquare] == null) {
                    return true
                }
            }
        }
        return false
    }*/

    fun tryPlay(s: String, s1: String): Board {
        check(canPlay(s, s1)) { "Invalid move $s to $s1" }
        return play(s, s1)
    }


}
