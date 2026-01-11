package com.hatem.noureddine.tictactoe.domain.model

/**
 * Represents the core logic of the Tic-Tac-Toe game.
 *
 * This class is responsible for managing the game state, including the board, the current player,
 * the winner, and whether the game is a draw. It is framework-agnostic and contains only pure
 * business logic.
 *
 * @property size The size of the game board (e.g., 3 for a 3x3 grid).
 * @param initialState An optional [GameState] to restore the game to a previous state.
 */
class Game(
    val size: Int = DEFAULT_BOARD_SIZE,
    initialState: GameState? = null,
) {
    /**
     * The player whose turn it is.
     */
    var currentPlayer: Player = initialState?.currentPlayer ?: Player.X
        private set

    /**
     * The winner of the game, or `null` if there is no winner yet.
     */
    var winner: Player? = initialState?.winner
        private set

    /**
     * `true` if the game is a draw, `false` otherwise.
     */
    var isDraw: Boolean = initialState?.isDraw ?: false
        private set

    // Internal representation of the game board.
    private val board = Array(size) { arrayOfNulls<Player>(size) }
    private var moveCount = 0

    init {
        // If an initial state is provided, restore the board from it.
        if (initialState != null) {
            restore(initialState)
        }
    }

    /**
     * Places the [currentPlayer]'s mark on the specified cell.
     *
     * After the move, it checks for a win or a draw and updates the game state accordingly.
     * If the game continues, it switches to the next player.
     *
     * @param row The row of the cell to play.
     * @param col The column of the cell to play.
     * @throws GameException if the game is over, the position is invalid, or the cell is already taken.
     */
    fun play(
        row: Int,
        col: Int,
    ) {
        validateMove(row, col)

        board[row][col] = currentPlayer
        moveCount++

        if (checkWin(currentPlayer)) {
            winner = currentPlayer
        } else if (moveCount == size * size) {
            isDraw = true
        } else {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }
    }

    /**
     * Allows the current player to forfeit the game, making the other player the winner.
     *
     * @throws GameException if the game is already over.
     */
    fun forfeit() {
        if (winner != null || isDraw) {
            throw GameException.GameOver()
        }
        winner = if (currentPlayer == Player.X) Player.O else Player.X
    }

    /**
     * Captures the current state of the game in an immutable [GameState] object.
     *
     * @return A snapshot of the current game state.
     */
    fun getSnapshot(): GameState {
        val boardMap = mutableMapOf<String, Player?>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                board[r][c]?.let {
                    boardMap["$r,$c"] = it
                }
            }
        }
        return GameState(
            board = boardMap,
            currentPlayer = currentPlayer,
            winner = winner,
            isDraw = isDraw,
            size = size,
        )
    }

    /**
     * Restores the game from a snapshot.
     */
    fun restore(snapshot: GameState) {
        // Clear board
        for (r in 0 until size) {
            for (c in 0 until size) {
                board[r][c] = null
            }
        }
        moveCount = 0

        // Restore from Map
        snapshot.board.forEach { (key, value) ->
            restoreCell(key, value)
        }
        currentPlayer = snapshot.currentPlayer
        winner = snapshot.winner
        isDraw = snapshot.isDraw
    }

    @Suppress("ThrowsCount")
    private fun validateMove(
        row: Int,
        col: Int,
    ) {
        if (winner != null || isDraw) {
            throw GameException.GameOver()
        }
        if (row !in 0 until size || col !in 0 until size) {
            throw GameException.InvalidPosition()
        }
        if (board[row][col] != null) {
            throw GameException.PositionTaken()
        }
    }

    private fun restoreCell(
        key: String,
        value: Player?,
    ) {
        val parts = key.split(",")
        if (parts.size == 2) {
            val r = parts[0].toIntOrNull()
            val c = parts[1].toIntOrNull()
            if (isValidSavedMove(r, c, value, size)) {
                board[r!!][c!!] = value
                moveCount++
            }
        }
    }

    private fun isValidSavedMove(
        r: Int?,
        c: Int?,
        value: Player?,
        size: Int,
    ): Boolean = r != null && c != null && value != null && r in 0 until size && c in 0 until size

    /**
     * Retrieves the player who has marked a specific cell.
     *
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @return The [Player] in the cell, or `null` if the cell is empty or the position is invalid.
     */
    fun getCell(
        row: Int,
        col: Int,
    ): Player? {
        if (row !in 0 until size || col !in 0 until size) {
            return null
        }
        return board[row][col]
    }

    /**
     * Checks if the specified player has won the game.
     *
     * @param player The player to check for a win condition.
     * @return `true` if the player has won, `false` otherwise.
     */
    private fun checkWin(player: Player): Boolean {
        val hasWinningRow = (0 until size).any { i -> (0 until size).all { j -> board[i][j] == player } }
        val hasWinningCol = (0 until size).any { i -> (0 until size).all { j -> board[j][i] == player } }
        val hasWinningDiagonal = (0 until size).all { i -> board[i][i] == player }
        val hasWinningAntiDiagonal = (0 until size).all { i -> board[i][size - 1 - i] == player }

        return hasWinningRow || hasWinningCol || hasWinningDiagonal || hasWinningAntiDiagonal
    }

    companion object {
        private const val DEFAULT_BOARD_SIZE = 3
    }
}
