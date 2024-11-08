package isel.leic.tds.checkers.model

class Diagonal(private val board: Board, private val startPos: Square) {
    private val directions = listOf(
        1 to 1, 1 to -1, -1 to 1, -1 to -1
    )

    private fun getPath(rowDir: Int, colDir: Int): List<Square> {
        return generateSequence(startPos) { square ->
            val nextRow = square.row.index + rowDir
            val nextCol = square.column.index + colDir
            if (nextRow in 0 until BOARD_DIM && nextCol in 0 until BOARD_DIM) {
                Square(Row(nextRow), Column(nextCol))
            } else {
                null
            }
        }.drop(1).toList()
    }

    private fun canCapture2(player: Player, rowDir: Int, colDir: Int): Boolean {
        val path = getPath(rowDir, colDir)
        val opponentSquares = path.takeWhile { square ->
            board.grid[square]?.player != player
        }

        return opponentSquares.size == 1 && opponentSquares.all { square ->
            board.grid[square]?.player == player.other
        } && path.drop(opponentSquares.size + 1).all { square ->
            board.grid[square] == null
        }
    }

    fun canCaptureInAnyDirection(player: Player): Boolean {
        return directions.any { (rowDir, colDir) ->
            canCapture2(player, rowDir, colDir)
        }
    }
}