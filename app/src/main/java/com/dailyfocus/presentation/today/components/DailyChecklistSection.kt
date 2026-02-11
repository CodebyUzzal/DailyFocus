package com.dailyfocus.presentation.today.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.dailyfocus.domain.model.DailyTaskInstance

/**
 * Section showing today's recurring task instances with checkboxes.
 */
@Composable
fun DailyChecklistSection(
    instances: List<DailyTaskInstance>,
    onToggle: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Daily Checklist",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (instances.isEmpty()) {
            Text(
                text = "No recurring tasks yet. Add some from the management screen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        } else {
            instances.forEach { instance ->
                DailyTaskItem(
                    title = instance.title,
                    isCompleted = instance.isCompleted,
                    onToggle = { onToggle(instance.id, instance.isCompleted) }
                )
            }
        }
    }
}

@Composable
fun DailyTaskItem(
    title: String,
    isCompleted: Boolean,
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
        onClick = onToggle // Make whole card clickable for checklist
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.m), // Inner padding
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
            }
        }
    }
}
