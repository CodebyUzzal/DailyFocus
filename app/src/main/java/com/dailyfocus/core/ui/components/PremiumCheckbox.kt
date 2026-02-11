package com.dailyfocus.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.AnimationConstants

/**
 * A premium checkbox with subtle scale and fade animation.
 * Avoids heavy Canvas drawing for performance.
 */
@Composable
fun PremiumCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animate scale: slightly scale down when checked
    val scale by animateFloatAsState(
        targetValue = if (checked) 0.9f else 1f,
        animationSpec = AnimationConstants.fastTween(),
        label = "checkboxScale"
    )

    // Animate color
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = AnimationConstants.fastTween(),
        label = "checkboxBg"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        animationSpec = AnimationConstants.fastTween(),
        label = "checkboxBorder"
    )

    Box(
        modifier = modifier
            .size(24.dp)
            .scale(scale)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null // Ripple handled by parent or custom if needed, keeping it clean here
            ) { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
