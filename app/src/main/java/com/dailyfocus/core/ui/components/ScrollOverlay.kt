package com.dailyfocus.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.GradientFadeEnd
import com.dailyfocus.core.ui.theme.GradientFadeStart

/**
 * Scroll Overlay.
 * Adds a gradient fade at the bottom of the screen to indicate scrollability.
 */
@Composable
fun ScrollOverlay(
    modifier: Modifier = Modifier,
    height: Dp = 96.dp, // Matches standard bottom padding
    colorStart: Color = GradientFadeStart,
    colorEnd: Color = GradientFadeEnd
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorStart, colorEnd)
                )
            )
    )
}
