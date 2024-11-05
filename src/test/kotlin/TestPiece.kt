import isel.leic.tds.checkers.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PieceTest {

    @Test
    fun testPawnCanMove() {
        val board = BoardRun(mapOf("2b".toSquare() to Pawn(Player.w)), Player.w)
        val pawn = board.grid["2b".toSquare()] ?: error("No piece at 2b")
        assertTrue(pawn.canMove(board, "2b".toSquare(), "3c".toSquare()))
        assertFalse(pawn.canMove(board, "2b".toSquare(), "4d".toSquare()))
    }

    @Test
    fun testPawnCanCapture() {
        val board = BoardRun(mapOf(
            "2b".toSquare() to Pawn(Player.w),
            "3c".toSquare() to Pawn(Player.b)
        ), Player.w)
        val pawn = board.grid["2b".toSquare()] ?: error("No piece at 2b")
        assertTrue(pawn.canCapture(board, "2b".toSquare()))
    }

    @Test
    fun testQueenCanMove() {
        val board = BoardRun(mapOf("4d".toSquare() to Queen(Player.w)), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertTrue(queen.canMove(board, "4d".toSquare(), "1a".toSquare()))
        assertTrue (queen.canMove(board, "4d".toSquare(), "7g".toSquare()))
        assertTrue(queen.canMove(board, "4d".toSquare(), "2f".toSquare()))
        assertTrue(queen.canMove(board, "4d".toSquare(), "5c".toSquare()))
        assertFalse(queen.canMove(board, "4d".toSquare(), "5f".toSquare()))
        assertFalse(queen.canMove(board, "4d".toSquare(), "7e".toSquare()))
    }

    @Test
    fun testQueenCanCapture() {
        val board = BoardRun(mapOf(
            "4d".toSquare() to Queen(Player.w),
            "7g".toSquare() to Pawn(Player.b)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertTrue(queen.canCapture(board, "7g".toSquare()))
    }

    @Test
    fun testPawnPromotion() {
        val pawnW = Pawn(Player.w)
        val pawnB = Pawn(Player.b)
        val queen = Queen(Player.w)
        val promotedPiece = pawnW.promote()
        val promotedPiece2 = pawnB.promote()
        val promotedPiece3 = queen.promote()
        assertTrue(promotedPiece is Queen, "Pawn should promote to Queen")
        assertTrue(promotedPiece2 is Queen, "Pawn should promote to Queen")
        assertEquals(queen, promotedPiece3, "Queen should not promote")
    }
}