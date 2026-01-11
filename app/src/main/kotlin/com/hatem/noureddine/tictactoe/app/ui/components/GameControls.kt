package com.hatem.noureddine.tictactoe.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.tictactoe.R

/**
 * Provides controls for game settings and actions.
 *
 * Includes a dialog for selecting board size (e.g., 3x3, 4x4) and a reset button.
 *
 * @param boardSize The current size of the board.
 * @param onBoardSizeChange Callback to update the board size.
 * @param onReset Callback to restart the game.
 * @param modifier Modifier for styling.
 */
@Composable
fun GameControls(
    boardSize: Int,
    onBoardSizeChange: (Int) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var buttonWidth by remember { mutableStateOf(0.dp) }
    val density = androidx.compose.ui.platform.LocalDensity.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Box {
            androidx.compose.material3.OutlinedButton(
                onClick = { expanded = true },
                shape =
                    androidx.compose.foundation.shape
                        .RoundedCornerShape(12.dp),
                border =
                    androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    ),
                modifier =
                    Modifier.onSizeChanged {
                        buttonWidth = with(density) { it.width.toDp() }
                    },
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Rounded.GridView,
                        // Decorative
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Text("Grid: ${boardSize}x$boardSize")
                    Icon(
                        imageVector =
                            if (expanded) {
                                androidx.compose.material.icons.Icons.Rounded.KeyboardArrowUp
                            } else {
                                androidx.compose.material.icons.Icons.Rounded.KeyboardArrowDown
                            },
                        contentDescription =
                            if (expanded) {
                                "Collapse grid selector"
                            } else {
                                "Expand grid selector"
                            },
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier =
                    Modifier
                        .width(buttonWidth)
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                shape =
                    androidx.compose.foundation.shape
                        .RoundedCornerShape(12.dp),
            ) {
                val sizes = listOf(3, 4, 5)
                sizes.forEachIndexed { index, size ->
                    val isSelected = size == boardSize
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${size}x$size",
                                fontWeight =
                                    if (isSelected) {
                                        androidx.compose.ui.text.font.FontWeight.Bold
                                    } else {
                                        androidx.compose.ui.text.font.FontWeight.Normal
                                    },
                            )
                        },
                        onClick = {
                            onBoardSizeChange(size)
                            expanded = false
                        },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Rounded.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                        contentPadding =
                            androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ),
                    )
                    if (index < sizes.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        androidx.compose.material3.OutlinedButton(
            onClick = onReset,
            shape =
                androidx.compose.foundation.shape
                    .RoundedCornerShape(12.dp),
            contentPadding =
                androidx.compose.foundation.layout
                    .PaddingValues(horizontal = 24.dp, vertical = 0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Rounded.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.restart_game))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameControlsPreview() {
    MaterialTheme {
        GameControls(
            boardSize = 3,
            onBoardSizeChange = {},
            onReset = {},
        )
    }
}
