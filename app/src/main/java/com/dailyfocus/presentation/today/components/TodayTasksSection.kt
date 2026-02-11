package com.dailyfocus.presentation.today.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TodayTask

/**
 * Section showing today's tasks using [DailyTaskItem].
 * Simplified: sub-items removed from the domain.
 */
@Composable
fun TodayTasksSection(
    tasks: List<TodayTask>,
    onToggleTask: (TodayTask) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.xs)
        )

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks for today. Tap + to add one.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.xxs)
            )
        } else {
            tasks.forEach { task ->
                DailyTaskItem(
                    title = task.title,
                    isCompleted = task.isCompleted,
                    category = task.category,
                    isRecurring = task.recurringTaskId != null,
                    onToggle = { onToggleTask(task) }
                )
            }
        }
    }
}
