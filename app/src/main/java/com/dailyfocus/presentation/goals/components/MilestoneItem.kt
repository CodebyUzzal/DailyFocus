package com.dailyfocus.presentation.goals.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.PremiumCheckbox
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.GoalItem

@Composable
fun MilestoneItem(
    item: GoalItem,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (item.isCompleted) 0.6f else 1f,
        label = "Milestone Alpha"
    )

    val textColor by animateColorAsState(
        targetValue = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        label = "Milestone Text Color"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Handle ripple manually or disable for cleaner look if preferred
            ) { onToggle() }
            .padding(vertical = 6.dp, horizontal = 4.dp) // optimized touch target
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumCheckbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() }
        )
        
        Spacer(modifier = Modifier.width(Spacing.s))
        
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
