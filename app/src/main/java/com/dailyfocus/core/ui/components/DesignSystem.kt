package com.dailyfocus.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation

/**
 * Premium Card with consistent elevation and shape.
 * defaulted to Surface color, with optional onclick.
 */
@Composable
fun DailyFocusCard(
    modifier: Modifier = Modifier,
    shape: Shape = AppShapes.Large,
    elevation: Dp = Elevation.Level1,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = { Box(modifier = Modifier.fillMaxSize()) { content() } } // simplified content
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = { Box(modifier = Modifier) { content() } }
        )
    }
}

/**
 * Premium Scaffold wrapper that handles the background gradient logic if needed,
 * or just ensures consistent background color.
 */
@Composable
fun DailyFocusScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        containerColor = containerColor,
        content = content
    )
}
