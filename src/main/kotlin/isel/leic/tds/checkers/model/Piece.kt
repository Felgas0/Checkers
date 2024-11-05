package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.model.*
import java.lang.Math.*
import kotlin.math.abs

abstract class Piece(val player: Player) {
    abstract fun canCapture(board: Board, startPos: Square): Boolean
    abstract fun canMove(board: Board, startPos: Square, endPos: Square): Boolean
    abstract fun promote(): Piece
    override fun toString(): String = if (player == Player.w) "w" else "b"
}

class Pawn(player: Player) : Piece(player) {

    override fun canCapture(board: Board, startPos: Square): Boolean {
        val directions = if (player == Player.w) listOf(-1 to -1, -1 to 1) else listOf(1 to -1, 1 to 1)

        for ((row, col) in directions) {
            val capturedRow = startPos.row.index + row
            val capturedCol = startPos.column.index + col
            val landingRow = startPos.row.index + 2 * row
            val landingCol = startPos.column.index + 2 * col
            if (capturedRow in 0 until BOARD_DIM && capturedCol in 0 until BOARD_DIM &&
                landingRow in 0 until BOARD_DIM && landingCol in 0 until BOARD_DIM
            ) {
                val capturePos = Square(Row(capturedRow), Column(capturedCol))

                val landingPos = Square(Row(landingRow), Column(landingCol))


                if (board.grid[capturePos]?.player == player.other && board.grid[landingPos] == null) {
                    return true
                }
            }
        }
        return false
    }

    override fun canMove(board: Board, startPos: Square, endPos: Square): Boolean {
        val rowDiff = endPos.row.index - startPos.row.index
        val colDiff = endPos.column.index - startPos.column.index

        // Check if the move is a simple move
        val isSimpleMove = when (player) {
            Player.w -> rowDiff == -1 && abs(colDiff) == 1
            Player.b -> rowDiff == 1 && abs(colDiff) == 1
        }

        // Check if the move is a capture
        val isCaptureMove = canCapture(board, startPos) && abs(rowDiff) == 2 && abs(colDiff) == 2

        return isSimpleMove || isCaptureMove
    }

    override fun promote(): Piece = Queen(player)

}


    class Queen(player: Player) : Piece(player) {

        private val moveDirections get() = (1..BOARD_DIM).flatMap { x ->
            listOf(
                Pair(x, x),
                Pair(x, -x),
                Pair(-x, x),
                Pair(-x, -x)
                //todas as direções possíveis
            )
        }


        override fun canCapture(board: Board, from: Square): Boolean {
            val directions = listOf(
                1 to 1, 1 to -1, -1 to 1, -1 to -1
            )

            return directions.any { (rowDir, colDir) ->
                val possibleSquares = generateSequence(from) { square ->
                    val nextRow = square.row.index + rowDir
                    val nextCol = square.column.index + colDir
                    if (nextRow in 0 until BOARD_DIM && nextCol in 0 until BOARD_DIM) {
                        Square(Row(nextRow), Column(nextCol))
                    } else {
                        null
                    }
                }.toList()

                val opponentSquares = possibleSquares.takeWhile { square ->
                    board.grid[square] == null || board.grid[square]?.player == this.player
                }

                val emptySquares = possibleSquares.takeWhile { square ->
                    board.grid[square] == null
                }

                opponentSquares.isNotEmpty() && opponentSquares.drop(1).all { square ->
                    board.grid[square] == null
                } && opponentSquares.firstOrNull()?.let { board.grid[it]?.player != this.player } == true
            }
        }

        override fun canMove(board: Board, startPos: Square, endPos: Square): Boolean {
            val rowDiff = endPos.row.index - startPos.row.index
            val colDiff = endPos.column.index - startPos.column.index

            // Check if the move is diagonal
            return moveDirections.any { (rowOffset, colOffset) ->
                rowDiff == rowOffset && colDiff == colOffset && endPos.isValid() && board.grid[endPos] == null
            }
        }

        override fun promote(): Piece = this

        override fun toString(): String = if (player == Player.w) "W" else "B"


        private fun checkDirectionForCapture(board: Board, startPos: Square, rowInc: Int, colInc: Int): Boolean {
            val nextRow = startPos.row.index + rowInc
            val nextCol = startPos.column.index + colInc

            if (nextRow !in 0 until BOARD_DIM || nextCol !in 0 until BOARD_DIM) return false

            val nextSquare = Square(Row(nextRow), Column(nextCol))
            val piece = board.grid[nextSquare]

            return when {
                piece == null -> checkDirectionForCapture(board, nextSquare, rowInc, colInc)
                piece.player != this.player -> {
                    val landingRow = nextRow + rowInc
                    val landingCol = nextCol + colInc
                    if (landingRow in 0 until BOARD_DIM && landingCol in 0 until BOARD_DIM) {
                        val landingSquare = Square(Row(landingRow), Column(landingCol))
                        board.grid[landingSquare] == null
                    } else {
                        false
                    }
                }
                else -> false
            }
        }
    }

