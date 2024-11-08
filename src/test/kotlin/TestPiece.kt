import isel.leic.tds.checkers.model.*
import kotlin.test.*

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
            "6f".toSquare() to Pawn(Player.b)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertTrue(queen.canCapture(board, "4d".toSquare()))
    }

    @Test
    fun testPlayWithNoCapture(){
        val board = BoardRun(mapOf(
            "1a".toSquare() to Queen(Player.b),
            "7g".toSquare() to Pawn(Player.b),
            "7a".toSquare() to Pawn(Player.b),
            "8b".toSquare() to Pawn(Player.b),
            "8d".toSquare() to Pawn(Player.b),
            "8f".toSquare() to Pawn(Player.b),
            "7g".toSquare() to Pawn(Player.b),
            "6h".toSquare() to Pawn(Player.b),
            "4h".toSquare() to Pawn(Player.w),
            "3g".toSquare() to Pawn(Player.w),
            "2h".toSquare() to Pawn(Player.w),
            "1g".toSquare() to Pawn(Player.w),
            "2d".toSquare() to Pawn(Player.w),
            ), Player.b)
        val newBoard = board.play("7a".toSquare(), "6b".toSquare())
        assertNotEquals(board, newBoard)
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
    @Test
    fun testQueenCannotCaptureSamePlayerPiece() {
        val board = BoardRun(mapOf(
            "4d".toSquare() to Queen(Player.w),
            "6f".toSquare() to Pawn(Player.w)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertFalse(queen.canCapture(board, "4d".toSquare()), "Queen should not be able to capture a piece of the same player")
    }

    @Test
    fun testQueenCanCaptureOpponentPiece() {
        val board = BoardRun(mapOf(
            "4d".toSquare() to Queen(Player.w),
            "6f".toSquare() to Pawn(Player.b)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertTrue(queen.canCapture(board, "4d".toSquare()), "Queen should be able to capture an opponent's piece")
    }

    @Test
    fun testQueenCannotCaptureIfBlockedBySamePlayerPiece() {
        val board = BoardRun(mapOf(
            "4d".toSquare() to Queen(Player.w),
            "5e".toSquare() to Pawn(Player.w),
            "6f".toSquare() to Pawn(Player.b)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertFalse(queen.canCapture(board, "4d".toSquare()), "Queen should not be able to capture if blocked by a piece of the same player")
    }

    @Test
    fun testQueenCanCaptureIfNotBlocked() {
        val board = BoardRun(mapOf(
            "4d".toSquare() to Queen(Player.w),
            "5e".toSquare() to Pawn(Player.b),
            "7g".toSquare() to Pawn(Player.b)
        ), Player.w)
        val queen = board.grid["4d".toSquare()] ?: error("No piece at 4d")
        assertTrue(queen.canCapture(board, "4d".toSquare()), "Queen should be able to capture if not blocked by a piece of the same player")
    }
}