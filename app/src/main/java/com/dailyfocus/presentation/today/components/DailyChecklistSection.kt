package com.dailyfocus.presentation.today.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumCheckbox
import com.dailyfocus.core.ui.theme.AnimationConstants
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskCategory

/**
 * Individual task item used in the Today screen.
 * Unified component for both recurring and manual tasks.
 */
@Composable
fun DailyTaskItem(
    title: String,
    isCompleted: Boolean,
    category: TaskCategory = TaskCategory.PERSONAL,
    isRecurring: Boolean = false,
    onToggle: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (isCompleted)
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.onSurface,
        label = "checklistTextColor",
        animationSpec = AnimationConstants.fastTween()
    )

    DailyFocusCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.xxs),
        elevation = Elevation.Level1,
        shape = AppShapes.Medium,
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PremiumCheckbox(
                checked = isCompleted,
                onCheckedChange = { onToggle() }
            )
            Spacer(modifier = Modifier.width(Spacing.m))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isRecurring) {
                        Text(
                            text = "• Recurring",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}
