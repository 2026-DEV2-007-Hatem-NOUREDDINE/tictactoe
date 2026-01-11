package com.hatem.noureddine.tictactoe.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hatem.noureddine.tictactoe.app.ui.GameScreenContent
import com.hatem.noureddine.tictactoe.app.ui.viewmodel.BoardUiState
import com.hatem.noureddine.tictactoe.domain.model.Player
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class GameScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gameScreen_verifiesInitialState() {
        val initialState =
            BoardUiState(
                boardSize = 3,
                board =
                    persistentListOf(
                        persistentListOf(null, null, null),
                        persistentListOf(null, null, null),
                        persistentListOf(null, null, null),
                    ),
                currentPlayer = Player.X,
            )

        composeTestRule.setContent {
            GameScreenContent(
                uiState = initialState,
                onPlay = { _, _ -> },
                onReset = {},
                onBoardSizeChange = {},
                onClearError = {},
            )
        }
        // Then
        composeTestRule.onNodeWithText("Current Player:").assertIsDisplayed()
        // Player X is shown as an Icon with content description "X" (from Player.name)
        composeTestRule.onNodeWithContentDescription("X").assertIsDisplayed()
        // Check for empty cells (content description)
        composeTestRule.onNodeWithContentDescription("Empty cell at row 1, column 1").assertIsDisplayed()
    }

    @Test
    fun gameScreen_verifiesCellClick() {
        var clickedRow = -1
        var clickedCol = -1
        val initialState = BoardUiState()

        composeTestRule.setContent {
            GameScreenContent(
                uiState = initialState,
                onPlay = { r, c ->
                    clickedRow = r
                    clickedCol = c
                },
                onReset = {},
                onBoardSizeChange = {},
                onClearError = {},
            )
        }

        composeTestRule.onNodeWithContentDescription("Empty cell at row 1, column 1").performClick()

        assert(clickedRow == 0)
        assert(clickedCol == 0)
    }

    @Test
    fun gameScreen_verifiesWinnerState() {
        val winnerState =
            BoardUiState(
                winner = Player.O,
            )

        composeTestRule.setContent {
            GameScreenContent(
                uiState = winnerState,
                onPlay = { _, _ -> },
                onReset = {},
                onBoardSizeChange = {},
                onClearError = {},
            )
        }

        // Check for "Winner:" label
        composeTestRule.onNodeWithText("Winner:").assertIsDisplayed()
        // Check for the icon description "Winner: O" (Player.name is used in GameStatus)
        composeTestRule.onNodeWithContentDescription("Winner: O").assertIsDisplayed()
    }

    @Test
    fun gameScreen_verifiesReset() {
        var resetCalled = false
        val uiState = BoardUiState()

        composeTestRule.setContent {
            GameScreenContent(
                uiState = uiState,
                onPlay = { _, _ -> },
                onReset = { resetCalled = true },
                onBoardSizeChange = {},
                onClearError = {},
            )
        }

        composeTestRule.onNodeWithText("Restart Game").performClick()
        assert(resetCalled)
    }
}
