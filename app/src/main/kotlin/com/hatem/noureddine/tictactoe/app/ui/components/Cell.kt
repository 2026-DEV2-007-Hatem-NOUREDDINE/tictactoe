package com.hatem.noureddine.tictactoe.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.tictactoe.R
import com.hatem.noureddine.tictactoe.app.ui.theme.PlayerOColor
import com.hatem.noureddine.tictactoe.app.ui.theme.PlayerXColor
import com.hatem.noureddine.tictactoe.domain.model.Player

/**
 * Renders a single cell within the game board.
 *
 * Displays the player's move (X or O) or responds to click events if empty.
 * Supports accessibility via content descriptions and haptic feedback on interaction.
 *
 * @param row The row index of this cell.
 * @param col The column index of this cell.
 * @param player The player occupying this cell, or null if empty.
 * @param onClick Callback invoked when the cell is clicked.
 * @param enabled True if the cell allows interaction.
 * @param size The size of the cell in dp.
 * @param modifier Modifier for styling.
 */
@Composable
fun Cell(
    row: Int,
    col: Int,
    player: Player?,
    onClick: () -> Unit,
    enabled: Boolean,
    size: Int,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val cellDescription =
        if (player == null) {
            stringResource(R.string.cd_cell_empty, row + 1, col + 1)
        } else {
            val playerName =
                if (player == Player.X) {
                    stringResource(R.string.cd_player_x)
                } else {
                    stringResource(R.string.cd_player_o)
                }
            stringResource(R.string.cd_cell_filled, row + 1, col + 1, playerName)
        }

    Card(
        modifier =
            modifier
                .size(size.dp)
                .padding(4.dp)
                .semantics { contentDescription = cellDescription }
                .testTag("cell-$row-$col")
                .clickable(enabled = enabled && player == null) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = player != null,
                enter = androidx.compose.animation.scaleIn() + androidx.compose.animation.fadeIn(),
            ) {
                player?.let {
                    val imageVector = if (it == Player.X) Icons.Rounded.Close else Icons.Rounded.RadioButtonUnchecked
                    val tint = if (it == Player.X) PlayerXColor else PlayerOColor
                    Icon(
                        imageVector = imageVector,
                        // Decorative
                        contentDescription = null,
                        modifier = Modifier.size((size * 0.6f).dp),
                        tint = tint,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CellPreview() {
    MaterialTheme {
        Cell(
            row = 0,
            col = 0,
            player = Player.X,
            onClick = {},
            enabled = true,
            size = 100,
        )
    }
}
