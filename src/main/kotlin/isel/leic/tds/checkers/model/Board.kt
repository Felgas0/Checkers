package isel.leic.tds.checkers.model

class Board(val grid: Map<Square, Piece?>, val player: Player) {

    companion object {
        fun createInitialBoard(player: Player): Board {
            val initialBoard = Square.values.fold(emptyMap<Square, Piece?>()) { acc, square ->
                when {
                    square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black -> acc + (square to Pawn(Player.b))
                    square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black -> acc + (square to Pawn(Player.w))
                    else -> acc
                }
            }
            return Board(initialBoard, player)
        }
    }


    fun play(startPos: Square, endPos: Square): Board {
        val newBoard = grid + (endPos to grid[startPos]) + (startPos to null)

        // Check if a piece was captured
        val capturedBoard = capture(newBoard, endPos, startPos)

        // Promote to checker if reaching the opposite end
        val piece = capturedBoard[endPos]
        val promotedBoard = if (piece != null) {
            when {
                player == Player.w && endPos.row.index == 0 -> capturedBoard + (endPos to piece.promote())
                player == Player.b && endPos.row.index == BOARD_DIM - 1 -> capturedBoard + (endPos to piece.promote())
                else -> capturedBoard
            }
        } else {
            capturedBoard
        }

        return Board(promotedBoard, player.other)
    }

    private fun capture(board: Map<Square, Piece?>, endPos: Square, startPos: Square): Map<Square, Piece?> {
        val rowDiff = endPos.row.index - startPos.row.index
        val colDiff = endPos.column.index - startPos.column.index
        return if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
            val capturedRow = startPos.row.index + rowDiff / 2
            val capturedCol = startPos.column.index + colDiff / 2
            val capturedSquare = Square(Row(capturedRow), Column(capturedCol))
            board + (capturedSquare to null)
        } else {
            board
        }
    }

    fun canPlay(startPos: Square, endPos: Square): Boolean {
        return when {
            grid[startPos] == null -> false          // if the selected square by the user does not have any piece can't play
            grid[startPos]?.player != player -> false          // if the piece selected by a player is an opponent piece can't play
            grid[endPos] != null -> false          // if there is a piece on the end position can't play
            !endPos.black -> false
            !isDiagonal(startPos, endPos) -> false
            else -> true
        }
    }

    fun isWinner(p: Player): Boolean = grid.values.none { it?.player != player }

    fun tryPlay(s: Square, s1: Square): Board {
        check(canPlay(s, s1)) { "Invalid move $s to $s1" }
        return play(s, s1)
    }
}