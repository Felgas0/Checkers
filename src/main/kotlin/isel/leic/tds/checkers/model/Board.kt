package isel.leic.tds.checkers.model


class Board(val board: MutableMap<Square, Player?>, val player: Player) {

    init {
        reset()
    }

    private fun reset() {
        if (board.isEmpty()) {
            // Initialize BLACK pieces
            Square.values.forEach { square ->
                if (square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black) {
                    board[square] = Player.b
                }
            }

            // Initialize WHITE pieces
            Square.values.forEach { square ->
                if (square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black) {
                    board[square] = Player.w
                }
            }
        }
    }

    fun play(startPos: Square, endPos: Square): Board {
        board[endPos] = board[startPos]
        board[startPos] = null

        // Check if a piece was captured
        capture(endPos, startPos)

        // Promote to checker if reaching the opposite end
        val piece = board[endPos]
        if (piece != null) {
            if (piece == Player.w && endPos.row.index == 0) {
                board[endPos] = Player.w
            } else if (piece == Player.b && endPos.row.index == BOARD_DIM - 1) {
                board[endPos] = Player.b
            }
        }

        return Board(board, player.other)
    }

    private fun capture(endPos: Square, startPos: Square) {
        val rowDiff = endPos.row.index - startPos.row.index
        val colDiff = endPos.column.index - startPos.column.index
        if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            val capturedRow = startPos.row.index + rowDiff / 2
            val capturedCol = startPos.column.index + colDiff / 2
            val capturedSquare = Square(Row(capturedRow), Column(capturedCol))
            board[capturedSquare] = null
        }
    }


    fun Board.canPlay(startPos: Square, endPos: Square): Boolean {
        return when {
            startPos == null || endPos == null -> false  // if the positions are not squares can't play
            board[startPos] == null -> false          // if the selected square by the user does not have any piece can't play
            board[startPos] != player -> false          // if the piece selected by a player is an opponent piece can't play
            board[endPos] != null -> false          // if there is a piece on the end position can't play
            !endPos.black -> false
            !isDiagonal(startPos, endPos) -> false
            anyPieceCanCapture() -> canCapture(startPos) // checks if the move is to capture a piece if he can capture any
            else -> true
        }
    }


    fun Board.isWinner(p: Player): Boolean = board.values.none(){it != player}

    private fun anyPieceCanCapture(): Boolean = board.filter { it.value == player }.keys.any() { canCapture(it)}

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

                if (board[midSquare] != null && board[midSquare] != player && board[newSquare] == null) {
                    return true
                }
            }
        }
        return false
    }

    fun tryPlay(s: Square, s1: Square): Board {
        check(canPlay(s, s1)) { "Invalid move $s to $s1" }
        return play(s, s1)
    }


}
