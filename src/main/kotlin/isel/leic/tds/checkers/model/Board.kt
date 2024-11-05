package isel.leic.tds.checkers.model

import kotlin.math.abs

typealias Grid = Map<Square, Piece>

sealed class Board(val grid: Grid){
    override fun hashCode(): Int {
        return grid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false

        return grid == other.grid
    }
}
class BoardRun(grid: Grid, val turn: Player) : Board(grid){
    override fun hashCode(): Int {
        return 31 * super.hashCode() + turn.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BoardRun) return false

        return turn == other.turn
    }
}
class BoardWin(grid: Grid, val winner: Player) : Board(grid){
    override fun hashCode(): Int {
        return 7 * super.hashCode() + winner.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BoardWin) return false

        return winner == other.winner
    }
}

operator fun Board.get(position: Square): Player? = grid[position]?.player

fun createInitialBoard(player: Player): BoardRun {
    val initialBoard = Square.values.fold(emptyMap<Square, Piece>()) { acc, square ->
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
    require(grid[from] != null) { "No piece at $from" }
    require(grid[from]?.player == turn) { "Not your turn" }

    val piece = grid[from] ?: error("No piece at $from")

    // Check if there are any possible captures for the current player
    val possibleCaptures = grid.filterValues { it.player == turn }
        .any { (square, piece) -> piece.canCapture(this, square) }

    val possibleCapture = grid.filterValues { it.player == turn }.filter { (square, piece) -> piece.canCapture(this, square) }

    // Use require to enforce capture rule
    require(!(possibleCaptures && !piece.canCapture(this, from))) {
        "You must capture if a capture is available, you can capture with the pieces in ${possibleCapture.keys}"
    }

    return (this as BoardRun).makeMove(from, to)
}

private fun BoardRun.makeMove(from: Square, to: Square): Board {
    val piece = grid[from] ?: error("No piece at $from")

    // Perform the move
    val newGrid = grid - from + (to to piece)
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

    // Check if the move is a capture move
    if (abs(rowDiff) == abs(colDiff) && abs(rowDiff) > 1) {
        val capturedSquares = (1 until abs(rowDiff)).map { i ->
            val capturedRow = from.row.index + i * (rowDiff / abs(rowDiff))
            val capturedCol = from.column.index + i * (colDiff / abs(colDiff))
            Square(Row(capturedRow), Column(capturedCol))
        }

        // Remove the captured pieces from the grid
        return capturedSquares.fold(grid) { acc, square -> acc - square }
    }

    return grid
}

private fun promote(grid: Grid, to: Square, piece: Piece): Grid {
    return if ((piece.player == Player.w && to.row.index == 0) || (piece.player == Player.b && to.row.index == BOARD_DIM - 1)) {
        grid + (to to piece.promote())
    } else {
        grid
    }
}

private fun checkWin(grid: Grid): Boolean {
    return grid.values.none { it.player == Player.b } || grid.values.none { it.player == Player.w }
}