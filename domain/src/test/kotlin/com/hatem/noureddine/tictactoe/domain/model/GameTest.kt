package com.hatem.noureddine.tictactoe.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameTest {
    @Test
    fun `initial state is correct`() {
        val game = Game()
        assertEquals(Player.X, game.currentPlayer)
        assertNull(game.winner)
        assertFalse(game.isDraw)
    }

    @Test
    fun `playing a move updates current player`() {
        val game = Game()
        game.play(0, 0)
        assertEquals(Player.O, game.currentPlayer)
    }

    @Test
    fun `playing a move updates the board`() {
        val game = Game()
        game.play(0, 0)
        assertEquals(Player.X, game.getCell(0, 0))
    }

    @Test
    fun `playing a winning move updates the winner`() {
        val game = Game()
        game.play(0, 0) // X
        game.play(1, 0) // O
        game.play(0, 1) // X
        game.play(1, 1) // O
        game.play(0, 2) // X
        assertEquals(Player.X, game.winner)
    }

    @Test
    fun `playing a diagonal winning move updates the winner`() {
        val game = Game()
        game.play(0, 0) // X
        game.play(0, 1) // O
        game.play(1, 1) // X
        game.play(0, 2) // O
        game.play(2, 2) // X
        assertEquals(Player.X, game.winner)
    }

    @Test
    fun `playing an anti-diagonal winning move updates the winner`() {
        val game = Game()
        game.play(0, 2) // X
        game.play(0, 0) // O
        game.play(1, 1) // X
        game.play(0, 1) // O
        game.play(2, 0) // X
        assertEquals(Player.X, game.winner)
    }

    @Test
    fun `playing until a draw updates isDraw`() {
        val game = Game()
        // Sequence for a draw
        game.play(0, 0) // X
        game.play(1, 1) // O
        game.play(0, 1) // X
        game.play(0, 2) // O
        game.play(2, 0) // X
        game.play(1, 0) // O
        game.play(1, 2) // X
        game.play(2, 1) // O
        game.play(2, 2) // X
        assertTrue(game.isDraw)
        assertNull(game.winner)
    }

    @Test
    fun `playing after a win throws exception`() {
        val game = Game()
        game.play(0, 0) // X
        game.play(1, 0) // O
        game.play(0, 1) // X
        game.play(1, 1) // O
        game.play(0, 2) // X

        assertThrows<GameException.GameOver> {
            game.play(1, 2) // O
        }
    }

    @Test
    fun `playing in a taken position throws exception`() {
        val game = Game()
        game.play(0, 0) // X

        assertThrows<GameException.PositionTaken> {
            game.play(0, 0) // O
        }
    }

    @Test
    fun `forfeiting a game declares the other player the winner`() {
        val game = Game()
        game.forfeit()
        assertEquals(Player.O, game.winner) // Player X starts, so O wins
    }

    @Test
    fun `getSnapshot returns correct state`() {
        val game = Game()
        game.play(0, 0) // X plays

        val snapshot = game.getSnapshot()

        assertEquals(Player.X, snapshot.board["0,0"])
        assertEquals(Player.O, snapshot.currentPlayer)
        assertFalse(snapshot.isDraw)
        assertNull(snapshot.winner)
    }

    @Test
    fun `restore recreates game state from snapshot`() {
        val originalGame = Game()
        originalGame.play(0, 0) // X plays
        val snapshot = originalGame.getSnapshot()

        val restoredGame = Game()
        restoredGame.restore(snapshot)

        assertEquals(Player.X, restoredGame.getCell(0, 0))
        assertEquals(Player.O, restoredGame.currentPlayer)
        // Verify board internals (moveCount logic indirectly)
        restoredGame.play(0, 1) // O plays
        assertEquals(Player.O, restoredGame.getCell(0, 1))
    }
}
