package isel.leic.tds.checkers.model

import kotlin.test.*
import isel.leic.tds.checkers.model.*

class PieceTest {
    /*

    @Test
    fun testPawnCanMove() {
        val board = createInitialBoard(Player.w)
        val pawn = Pawn(Player.w)
        val startPos = "3c".toSquare()
        val moves = pawn.moves(board, startPos)
        assertTrue (moves.size == 2, "Pawn should have 2 valid moves")
        assertTrue("4b".toSquare() in moves, "Pawn should have this valid move")
        assertTrue ( "4d".toSquare() in moves, "Pawn should have this valid move" )
    }


    @Test
    fun testPawnCanCapture() {
        val board = createInitialBoard(Player.w)
        val pawn = Pawn(Player.w)
        val startPos = Square(Row(1), Column(1))
        val captures = pawn.captures(board, startPos)
        assertTrue(captures.isNotEmpty(), "Pawn should have valid captures")
    }

    @Test
    fun testQueenCanMove() {
        val board = Board(emptyMap())
        val queen = Queen(Player.WHITE)
        val startPos = Square(Row(1), Column(1))
        val moves = queen.moves(board, startPos)
        assertTrue(moves!!.isNotEmpty(), "Queen should have valid moves")
    }

    @Test
    fun testQueenCanCapture() {
        val board = Board(mapOf(
            Square(Row(1), Column(1)) to Queen(Player.WHITE),
            Square(Row(2), Column(2)) to Pawn(Player.BLACK)
        ))
        val queen = Queen(Player.WHITE)
        val startPos = Square(Row(1), Column(1))
        val captures = queen.captures(board, startPos)
        assertTrue(captures.isNotEmpty(), "Queen should have valid captures")
    }

    @Test
    fun testPawnPromotion() {
        val pawn = Pawn(Player.WHITE)
        val promotedPiece = pawn.promote()
        assertTrue(promotedPiece is Queen, "Pawn should promote to Queen")
    }
     */
}