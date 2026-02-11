package com.dailyfocus.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.dailyfocus.core.ui.theme.AnimationConstants

/**
 * Premium Extended FAB with subtle scale animation on press.
 */
@Composable
fun PremiumExtendedFAB(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    expanded: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1.0f,
        animationSpec = AnimationConstants.fastTween(),
        label = "fabScale"
    )

    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        containerColor = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
        text = text,
        icon = icon,
        expanded = expanded
    )
}
