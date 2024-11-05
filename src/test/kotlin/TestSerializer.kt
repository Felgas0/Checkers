package isel.leic.tds.checkers.model

import kotlin.test.*

class SerializerTest {

    @Test
    fun testGameSerialization() {
        val game = Game(
            board = createInitialBoard(Player.w),
            score = mapOf(Player.w to 1, Player.b to 0, null to 0),
            firstPlayer = Player.w
        )

        val serializedGame = GameSerializer.serialize(game)
        val deserializedGame = GameSerializer.deserialize(serializedGame)

        assertNotNull(deserializedGame)
        assertEquals(game.firstPlayer, deserializedGame.firstPlayer)
        assertEquals(game.score, deserializedGame.score)
        assertEquals(game, deserializedGame)
    }

    @Test
    fun testBoardSerialization() {
        val board = createInitialBoard(Player.w)
        val serializedBoard = BoardSerializer.serialize(board)
        val deserializedBoard = BoardSerializer.deserialize(serializedBoard)

        assertNotNull(deserializedBoard)
        assertEquals(board, deserializedBoard)
        assertEquals((board as BoardRun).turn, (deserializedBoard as BoardRun).turn)
    }

    @Test
    fun testEmptyBoardSerialization() {
        val board = BoardRun(emptyMap(), Player.w)
        val serializedBoard = BoardSerializer.serialize(board)
        val deserializedBoard = BoardSerializer.deserialize(serializedBoard)

        assertNotNull(deserializedBoard)
        assertEquals(board, deserializedBoard)
    }

    @Test
    fun testDifferentPlayersSerialization() {
        val game = Game(
            board = createInitialBoard(Player.b),
            score = mapOf(Player.w to 2, Player.b to 3, null to 0),
            firstPlayer = Player.b
        )

        val serializedGame = GameSerializer.serialize(game)
        val deserializedGame = GameSerializer.deserialize(serializedGame)

        assertNotNull(deserializedGame)
        assertEquals(game.firstPlayer, deserializedGame.firstPlayer)
        assertEquals(game.score, deserializedGame.score)
        assertEquals(game, deserializedGame)
    }

    @Test
    fun testWinningBoardSerialization() {
        val board = BoardWin(createInitialBoard(Player.w).grid, Player.w)
        val serializedBoard = BoardSerializer.serialize(board)
        val deserializedBoard = BoardSerializer.deserialize(serializedBoard)

        assertNotNull(deserializedBoard)
        assertEquals(board, deserializedBoard)
        assertEquals((board as BoardWin).winner, (deserializedBoard as BoardWin).winner)
    }
}
