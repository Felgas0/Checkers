package isel.leic.tds.checkers.model


abstract class Piece(val player: Player) {

    abstract val isChecker:Boolean
    abstract fun canCapture(board: Board, startPos:Square):Boolean
    abstract fun canMove(board: Board, startPos: Square, endPos:Square):Boolean
    abstract fun promote():Piece

    override fun toString(): String = if (player == Player.w) "w" else "b"


}

class Pawn(player: Player) : Piece(player) {
    override val isChecker = false

    private val captureDirections = listOf(
        Pair(2, 2), Pair(2, -2), Pair(-2, 2), Pair(-2, -2)
    )

    private val moveDirections = listOf(
        Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)
    )

    override fun canCapture(board: Board, startPos: Square): Boolean {
        return captureDirections.any { (rowOffset, colOffset) ->
            val endPos = Square(
                Row(startPos.row.index + rowOffset),
                Column(startPos.column.index + colOffset)
            )

            if (endPos.isValid() && board.grid[endPos] == null) {
                val middlePos = Square(
                    Row(startPos.row.index + rowOffset / 2),
                    Column(startPos.column.index + colOffset / 2)
                )

                val middlePiece = board.grid[middlePos]
                middlePiece != null && middlePiece.player != player
            } else false
        }
    }

    override fun canMove(board: Board, startPos: Square, endPos: Square): Boolean {
        val rowDiff = endPos.row.index - startPos.row.index
        val colDiff = endPos.column.index - startPos.column.index

        // Check if the move is one square diagonally
        return moveDirections.any { (rowOffset, colOffset) ->
            rowDiff == rowOffset && colDiff == colOffset && endPos.isValid() && board.grid[endPos] == null
        }
    }

    override fun promote(): Piece = Queen(player)
}

class Queen(player: Player):Piece(player){
    override val isChecker = true

    private val captureDirections get()= (2..<BOARD_DIM).flatMap { x ->
        listOf(
            Pair(x, x),
            Pair(x, -x),
            Pair(-x, x),
            Pair(-x, -x)
            //todas as direções possíveis a partir de dois até Board_DIM-1
        )
    }

    private val moveDirections get()= (1..BOARD_DIM).flatMap { x ->
        listOf(
            Pair(x, x),
            Pair(x, -x),
            Pair(-x, x),
            Pair(-x, -x)
            //todas as direções possíveis
        )
    }

    override fun canCapture(board: Board, startPos: Square): Boolean {
        TODO("Not yet implemented")
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
}



