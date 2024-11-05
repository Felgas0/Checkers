package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.UI.show
import kotlin.test.*

class BoardTest {

    @Test
    fun testCreateInitialBoard() {
        val board = createInitialBoard(Player.w)
        assertNotNull(board)
        assertTrue(board is BoardRun)
        assertEquals(Player.w, (board as BoardRun).turn)
        assertNotNull(board.grid[Square(Row(0), Column(1))])
        assertNotNull(board.grid[Square(Row(7), Column(0))])
        assertEquals(Player.b, board.grid[Square(Row(0), Column(1))]?.player)
        assertEquals(Player.w, board.grid[Square(Row(7), Column(0))]?.player)
    }

    @Test
    fun testMakeMove() {
        val board = createInitialBoard(Player.w)
        val from = Square(Row(5), Column(0))
        val to = Square(Row(4), Column(1))
        val newBoard = board.play(from, to)
        assertTrue(newBoard is BoardRun)
        assertNull(newBoard.grid[from])
        assertNotNull(newBoard.grid[to])
    }

    @Test
    fun testCapturePiece() {
        val board = createInitialBoard(Player.w)
        val boardWithMove = board.play("3c".toSquare(), "4b".toSquare())
        val boardWithCapture = boardWithMove.play("6d".toSquare(), "5c".toSquare())
        val finalBoard = boardWithCapture.play("4b".toSquare(), "6d".toSquare())
        assertTrue(finalBoard is BoardRun)
        assertNull(finalBoard.grid["3c".toSquare()]) // Moved piece
        assertNull(finalBoard.grid["5c".toSquare()]) // Captured piece
        assertNotNull(finalBoard.grid["6d".toSquare()]) // Capturing piece
        assertEquals(Player.w, finalBoard.grid["6d".toSquare()]?.player)
    }

    @Test
    fun testPromotePiece() {
        //for white piece
        val grid = mapOf(
            "7a".toSquare() to Pawn(Player.w),
            "8h".toSquare() to Pawn(Player.b)
        )
        val boardBeforePromotion = BoardRun(grid, Player.w)
        val boardWithPromotion = boardBeforePromotion.play("7a".toSquare(), "8b".toSquare())
        assertTrue(boardWithPromotion is BoardRun)
        assertTrue(boardWithPromotion.grid[Square(Row(0), Column(1))] is Queen)

        //for black piece
        val grid2 = mapOf(
            "5c".toSquare() to Pawn(Player.w),
            "2f".toSquare() to Pawn(Player.b)
        )
        val boardBeforePromotion2 = BoardRun(grid2, Player.b)
        val boardWithPromotion2 = boardBeforePromotion2.play("2f".toSquare(), "1e".toSquare())
        assertTrue(boardWithPromotion2 is BoardRun)
        assertTrue(boardWithPromotion2.grid["1e".toSquare()] is Queen)
    }

    @Test
    fun testCheckWin() {
        val grid = mapOf(
            "4d".toSquare() to Pawn(Player.w),
            "5e".toSquare() to Pawn(Player.b)
        )
        val boardBeforeWin = BoardRun(grid, Player.w)
        val boardAfterWin = boardBeforeWin.play("4d".toSquare(), "6f".toSquare())
        assertTrue(boardAfterWin is BoardWin)
        assertEquals(Player.w, (boardAfterWin as BoardWin).winner)
    }

    @Test
    fun testCaptureMandatory() {
        val grid = mapOf(
            "4d".toSquare() to Pawn(Player.w),
            "5e".toSquare() to Pawn(Player.b),
            "3g".toSquare() to Pawn(Player.w)
        )
        val boardBeforeCapture = BoardRun(grid, Player.w)
        assertFailsWith<IllegalArgumentException>("You must capture if a capture is available") {
            boardBeforeCapture.play("3g".toSquare(), "4h".toSquare())
        }
    }

    @Test
    fun multipleCaptures(){
        val grid = mapOf(
            "1a".toSquare() to Pawn(Player.w),
            "2b".toSquare() to Pawn(Player.b),
            "4d".toSquare() to Pawn(Player.b),
            "6f".toSquare() to Pawn(Player.b)
        )
        val boardBeforeCapture = BoardRun(grid, Player.w)
        boardBeforeCapture.show()
        val boardAfterCapture = boardBeforeCapture.play("1a".toSquare(), "3c".toSquare())
        val boardAfterCapture2 = boardAfterCapture.play("3c".toSquare(), "5e".toSquare())
        val boardAfterCapture3 = boardAfterCapture2.play("5e".toSquare(), "7g".toSquare())
        assertTrue(boardAfterCapture2 is BoardRun)
        assertNull(boardAfterCapture2.grid["2b".toSquare()])
        assertNull(boardAfterCapture2.grid["4d".toSquare()])
        assertNull(boardAfterCapture3.grid["6f".toSquare()])
        assertTrue(boardAfterCapture3 is BoardWin)
        assertEquals(boardAfterCapture3.winner, Player.w)
        assertNotNull(boardAfterCapture3.grid["7g".toSquare()])
    }

    @Test
    fun queenCanCapturePawnMoveInstead(){
        val grid = mapOf(
            "8h".toSquare() to Queen(Player.w),
            "4d".toSquare() to Pawn(Player.b),
            "2f".toSquare() to Pawn(Player.w)
        )
        val boardBeforeCapture = BoardRun(grid, Player.w)
        assertFailsWith<IllegalArgumentException>("You must capture if a capture is available") {
            boardBeforeCapture.play("2f".toSquare(), "3g".toSquare())
        }
    }
}