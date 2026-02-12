package com.dailyfocus.core.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Radius
import com.dailyfocus.core.ui.theme.Spacing

/**
 * Standard DailyFocus Card.
 * Uses Surface 3/4 colors and standard radii.
 */
@Composable
fun DailyFocusCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Radius.medium),
    containerColor: Color = MaterialTheme.colorScheme.surface, // Defined as Surface3 in Theme
    elevation: Dp = Elevation.Level1,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = content
        )
    }
}
