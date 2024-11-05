package isel.leic.tds.checkers.model

class InvalidMoveException(message: String) : Exception(message)

typealias Grid = Map<Square, Piece?>

sealed class Board(val grid: Grid)
class BoardRun(grid: Grid, val turn: Player) : Board(grid)
class BoardWin(grid: Grid, val winner: Player) : Board(grid)

operator fun Board.get(position: Square): Player? = if(grid[position] != null) grid[position]?.player else null

fun createInitialBoard(player: Player): BoardRun {
    val initialBoard = Square.values.fold(emptyMap<Square, Piece?>()) { acc, square ->
        when {
            square.row.index in 0 until BOARD_DIM / 2 - 1 && square.black -> acc + (square to Pawn(Player.b))
            square.row.index in BOARD_DIM / 2 + 1 until BOARD_DIM && square.black -> acc + (square to Pawn(Player.w))
            else -> acc
        }
    }
    return BoardRun(initialBoard, player)
}

fun Board.play(from: Square, to: Square): Board {
    require(this is BoardRun) { "Game is over" }
    require(to.black && from.black) { "Invalid Position" }
    require(this[to] == null) { "Position taken $to" }
    require(grid[from] != null) { "No piece at $from" }
    require(grid[from]?.player == turn) { "Not your turn" }


    val piece = grid[from] ?: error("No piece at $from")

    // Check if there are any possible captures for the current player
    val possibleCaptures = grid.filterValues { it?.player == turn }
        .any { (square, piece) -> piece?.canCapture(this, square) == true }

    val possibleCapture = grid.filterValues { it?.player == turn }.filter { (square, piece) -> piece?.canCapture(this, square) == true }

    // Use require to enforce capture rule
    require(!(possibleCaptures && !piece.canCapture(this, from))) {
        "You must capture if a capture is available for example $possibleCapture"
    }

    return (this as BoardRun).makeMove(from, to)
}
//ver o canCapture antes de ver o canMove!!!
/*
private fun BoardRun.makeMove(from: Square, to: Square): Board {
    val piece = grid[from] ?: error("No piece at $from")
    val newGrid = if(piece.canMove(this,from,to)) grid + (to to piece) + (from to null) else error("Invalid move")
    val captures = piece.canCapture(this, from)
    val capturedGrid = check(captures[from] == to, )
    val promotedGrid = promote(capturedGrid, to, piece)
    return if (checkWin(promotedGrid)) BoardWin(promotedGrid, turn) else BoardRun(promotedGrid, turn.other)
}
*/

private fun BoardRun.makeMove(from: Square, to: Square): Board {
    val piece = grid[from] ?: error("No piece at $from")

    // Perform the move
    val newGrid = grid + (to to piece) + (from to null)
    require(piece.canMove(this, from, to)) { "Invalid move" }

    // Check if the move results in a capture
    val capturedGrid = if (piece.canCapture(this, from)) capture(newGrid, from, to) else newGrid

    // Promote the piece if necessary
    val promotedGrid = promote(capturedGrid, to, piece)

    // If a capture was made and more captures are available, return the board with the current player
    val nextTurn = if (capturedGrid != newGrid && piece.canCapture(BoardRun(promotedGrid, turn), to)) turn else turn.other

    // Check for a win condition
    return if (checkWin(promotedGrid)) BoardWin(promotedGrid, turn) else BoardRun(promotedGrid, nextTurn)
}

private fun capture(grid: Grid, from: Square, to: Square): Grid {
    val rowDiff = to.row.index - from.row.index
    val colDiff = to.column.index - from.column.index
    return if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 2) {
        val capturedRow = from.row.index + rowDiff / 2
        val capturedCol = from.column.index + colDiff / 2
        val capturedSquare = Square(Row(capturedRow), Column(capturedCol))
        grid + (capturedSquare to null)
    } else {
        grid
    }
}

private fun promote(grid: Grid, to: Square, piece: Piece): Grid {
    return if ((piece.player == Player.w && to.row.index == 0) || (piece.player == Player.b && to.row.index == BOARD_DIM - 1)) {
        grid + (to to piece.promote())
    } else {
        grid
    }
}

private fun checkWin(grid: Grid): Boolean {
    return grid.values.none { it?.player == Player.b } || grid.values.none { it?.player == Player.w }
}