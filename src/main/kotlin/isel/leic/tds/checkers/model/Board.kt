package isel.leic.tds.checkers.model

import java.util.Stack


class Board(val grid: MutableMap<Square, Player?>, val player: Player) {

    init {
        reset()
    }

    private fun reset() {
        if (grid.isEmpty()) {
            // Initialize BLACK pieces
            Square.values.forEach { square ->
                if (square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black) {
                    grid[square] = Player.b
                }
            }

            // Initialize WHITE pieces
            Square.values.forEach { square ->
                if (square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black) {
                    grid[square] = Player.w
                }
            }
        }
    }
/*
    fun play(startPos: Square, endPos: Square): Board {
        grid[endPos] = grid[startPos]
        grid[startPos] = null

        if (grid[endPos] == Player.w && endPos.row.index == 0) {
            grid[endPos] = Player.w
        } else if (grid[endPos] == Player.b && endPos.row.index == BOARD_DIM - 1) {
            grid[endPos] = Player.b
        }
        return Board(grid, player.other)
    }

 */

    fun play(startPos: Square, endPos: Square): Board {
        grid[endPos] = grid[startPos]
        grid[startPos] = null

        // Check if a piece was captured
        if(canCapture(startPos)) capture(startPos, endPos)
        else {
            if (grid[endPos] == Player.w && endPos.row.index == 0) {
                grid[endPos] = Player.w
            } else if (grid[endPos] == Player.b && endPos.row.index == BOARD_DIM - 1) {
                grid[endPos] = Player.b
            }
        }
        return Board(grid, player.other)
    }

    private fun canCapture2(endPos: Square, startPos: Square) {
        val rowDiff = endPos.row.index - startPos.row.index
        val colDiff = endPos.column.index - startPos.column.index
        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            val capturedRow = startPos.row.index + rowDiff / 2
            val capturedCol = startPos.column.index + colDiff / 2
            val capturedSquare = Square(Row(capturedRow), Column(capturedCol))
            grid[capturedSquare] = null
        }
    }


    fun Board.canPlay(startPos: Square, endPos: Square): Boolean {
        return when {
            startPos == null || endPos == null -> false  // if the positions are not squares can't play
            grid[startPos] == null -> false          // if the selected square by the user does not have any piece can't play
            grid[startPos] != player -> false          // if the piece selected by a player is an opponent piece can't play
            grid[endPos] != null -> false          // if there is a piece on the end position can't play
            anyPieceCanCapture() -> canCapture(startPos) // checks if the move is to capture a piece if he can capture any
            else -> true
        }
    }



    fun Board.isWinner(p: Player): Boolean = grid.values.none(){it != player}

    private fun anyPieceCanCapture(): Boolean = grid.filter { it.value == player }.keys.any() { canCapture(it)}

    private fun canCapture(startingSquare: Square): Boolean {
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

                if (grid[midSquare] != null && grid[midSquare] != player && grid[newSquare] == null) {
                    return true
                }
            }
        }
        return false
    }

    private fun capture(s1: Square, s2: Square): Board {
        val rowDiff = s2.row.index - s1.row.index
        val colDiff = s2.column.index - s1.column.index
        val capturedRow = s1.row.index + rowDiff / 2
        val capturedCol = s1.column.index + colDiff / 2
        val capturedSquare = Square(Row(capturedRow), Column(capturedCol))
        grid[capturedSquare] = null
        return play(s1, s2)
    }



    fun tryPlay(s: Square, s1: Square): Board {
        check(canPlay(s, s1)) { "Invalid move $s to $s1" }
        return play(s, s1)
    }


}
