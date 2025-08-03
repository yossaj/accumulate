package com.lingoal.accumulate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
fun AutoCompleteTextView(
    label: String,
    query: String,
    predictions: List<String>,
    onQueryChanged: (String) -> Unit,
    onDoneActionClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (String) -> Unit = {}
) {
    val queryFieldWidth = remember { mutableStateOf(0) }

    Box(modifier = modifier) {
        val focusManager = LocalFocusManager.current
        QuerySearch(
            label = label,
            query = query,
            onQueryChanged = onQueryChanged,
            onDoneActionClick = {
                focusManager.clearFocus()
                onDoneActionClick()
            },
            onClearClick = {
                onClearClick()
            },
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    queryFieldWidth.value = coordinates.size.width
                }
        )
        if (predictions.isNotEmpty()) {
            Predictions(
                width = queryFieldWidth.value,
                predictions = predictions,
                itemContent = itemContent
            )
        }
    }
}

@Composable
fun QuerySearch(
    label: String,
    query: String,
    modifier: Modifier = Modifier,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
) {
    var showClearButton by remember { mutableStateOf(query.isNotEmpty()) }

    OutlinedTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            },
        value = query,
        onValueChange = onQueryChanged,
        label = { Text(text = label) },
        singleLine = true,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }

        },
        keyboardActions = KeyboardActions(onDone = {
            onDoneActionClick()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
private fun Predictions(
    width: Int,
    predictions: List<String>,
    itemContent: @Composable (String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current
    val minHeight = TextFieldDefaults.MinHeight * 3
    var popupMaxHeight by remember { mutableStateOf(minHeight) }
    val widthDp = with(density) { width.toDp() }

    Popup(
        popupPositionProvider = PredictionsPositionProvider(
            minHeight = with(density) { minHeight.roundToPx() },
            onPositionCalculated = { maxHeight ->
                popupMaxHeight = with(density) { maxHeight.toDp() }
            }        ),
    ) {

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .width(widthDp)
                .heightIn(max = popupMaxHeight)
                .padding(horizontal = 16.dp),
            state = lazyListState,
        ) {
            items(predictions) { prediction ->
                itemContent(prediction)
            }
        }
    }
}


/**
 * Interfaces for positioning a popup within a window.
 */
@Stable
internal object PredictionPosition {
    /**
     * An interface to calculate the vertical position of a menu with respect to its anchor and
     * window. The returned y-coordinate is relative to the window.
     */
    @Stable
    fun interface Vertical {
        fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuHeight: Int,
        ): Int
    }

    /**
     * An interface to calculate the horizontal position of a menu with respect to its anchor,
     * window, and layout direction. The returned x-coordinate is relative to the window.
     */
    @Stable
    fun interface Horizontal {
        fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuWidth: Int,
            layoutDirection: LayoutDirection,
        ): Int
    }

    /**
     * Returns a [PredictionPosition.Horizontal] which aligns the start of the menu to the start of the
     * anchor.
     */
    fun startToAnchorStart(): Horizontal =
        AnchorAlignmentOffsetPosition.Horizontal(
            menuAlignment = Alignment.Start,
            anchorAlignment = Alignment.Start,
        )

    /**
     * Returns a [PredictionPosition.Vertical] which aligns the top of the menu to the bottom of the
     * anchor.
     */
    fun topToAnchorBottom(): Vertical =
        AnchorAlignmentOffsetPosition.Vertical(
            menuAlignment = Alignment.Top,
            anchorAlignment = Alignment.Bottom,
        )

    /**
     * Returns a [PredictionPosition.Vertical] which aligns the bottom of the menu to the top of the
     * anchor.
     */
    fun bottomToAnchorTop(): Vertical =
        AnchorAlignmentOffsetPosition.Vertical(
            menuAlignment = Alignment.Bottom,
            anchorAlignment = Alignment.Top,
        )
}

@Immutable
internal object AnchorAlignmentOffsetPosition {
    /**
     * A [PredictionPosition.Horizontal] which horizontally aligns the given [menuAlignment] with the
     * given [anchorAlignment].
     */
    @Immutable
    data class Horizontal(
        private val menuAlignment: Alignment.Horizontal,
        private val anchorAlignment: Alignment.Horizontal,
    ) : PredictionPosition.Horizontal {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuWidth: Int,
            layoutDirection: LayoutDirection,
        ): Int {
            val anchorAlignmentOffset = anchorAlignment.align(
                size = 0,
                space = anchorBounds.width,
                layoutDirection = layoutDirection,
            )
            val menuAlignmentOffset = -menuAlignment.align(
                size = 0,
                space = menuWidth,
                layoutDirection,
            )
            return anchorBounds.left + anchorAlignmentOffset + menuAlignmentOffset
        }
    }

    @Immutable
    data class Vertical(
        private val menuAlignment: Alignment.Vertical,
        private val anchorAlignment: Alignment.Vertical,
    ) : PredictionPosition.Vertical {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuHeight: Int,
        ): Int {
            val anchorAlignmentOffset = anchorAlignment.align(
                size = 0,
                space = anchorBounds.height,
            )
            val menuAlignmentOffset = -menuAlignment.align(
                size = 0,
                space = menuHeight,
            )
            return anchorBounds.top + anchorAlignmentOffset + menuAlignmentOffset
        }
    }
}

@Composable
fun PredictionItem(
    prediction: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                focusManager.clearFocus()
                onItemClick(prediction)
            }
            .padding(1.dp)
    ) {
        Text(text = prediction, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Immutable
internal data class PredictionsPositionProvider(
    val minHeight: Int,
    val onPositionCalculated: (popupMaxHeight: Int) -> Unit = { }
) : PopupPositionProvider {
    // Horizontal position
    private val startToAnchorStart: PredictionPosition.Horizontal = PredictionPosition.startToAnchorStart()

    // Vertical position
    private val topToAnchorBottom: PredictionPosition.Vertical = PredictionPosition.topToAnchorBottom()
    private val bottomToAnchorTop: PredictionPosition.Vertical = PredictionPosition.bottomToAnchorTop()

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val x = startToAnchorStart.position(
            anchorBounds = anchorBounds,
            windowSize = windowSize,
            menuWidth = popupContentSize.width,
            layoutDirection = layoutDirection
        )

        val (y, maxHeight) = if (minHeight < windowSize.height - anchorBounds.bottom || minHeight >= anchorBounds.top) {
            topToAnchorBottom.position(
                anchorBounds = anchorBounds,
                windowSize = windowSize,
                menuHeight = popupContentSize.height
            ) to windowSize.height - anchorBounds.bottom
        } else {
            bottomToAnchorTop.position(
                anchorBounds = anchorBounds,
                windowSize = windowSize,
                menuHeight = minHeight.coerceAtMost(popupContentSize.height)
            ) to minHeight.coerceAtMost(popupContentSize.height)
        }

        val menuOffset = IntOffset(x, y)
        onPositionCalculated(maxHeight)
        return menuOffset
    }
}