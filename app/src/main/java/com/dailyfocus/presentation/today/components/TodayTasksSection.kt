package com.dailyfocus.presentation.today.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumCheckbox
import com.dailyfocus.core.ui.theme.AnimationConstants
import com.dailyfocus.core.ui.theme.AppShapes
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.presentation.today.TodayTaskWithItems

/**
 * Section showing one-time today tasks with expandable sub-items.
 */
@Composable
fun TodayTasksSection(
    tasks: List<TodayTaskWithItems>,
    onToggleTask: (TodayTask) -> Unit,
    onToggleItem: (TaskItem, TodayTask) -> Unit,
    onAddSubItem: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Priorities", // Renamed to Priorities to match TodayScreen, or keep Today Tasks? Plan said "Priorities" in TodayScreen.
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary, // Using primary color for section header
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
            tasks.forEach { taskWithItems ->
                TodayTaskCard(
                    taskWithItems = taskWithItems,
                    onToggleTask = { onToggleTask(taskWithItems.task) },
                    onToggleItem = { item -> onToggleItem(item, taskWithItems.task) },
                    onAddSubItem = onAddSubItem
                )
            }
        }
    }
}

@Composable
fun TodayTaskCard(
    taskWithItems: TodayTaskWithItems,
    onToggleTask: () -> Unit,
    onToggleItem: (TaskItem) -> Unit,
    onAddSubItem: (Long, String) -> Unit
) {
    val task = taskWithItems.task
    val items = taskWithItems.items
    var expanded by remember { mutableStateOf(items.isNotEmpty()) }
    var showAddItem by remember { mutableStateOf(false) }
    var newItemTitle by remember { mutableStateOf("") }

    val textColor by animateColorAsState(
        targetValue = if (task.isCompleted)
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.onSurface,
        label = "taskTextColor",
        animationSpec = AnimationConstants.fastTween()
    )

    // Using basic Card or DailyFocusCard? 
    // Plan says "DailyFocusCard (Using new Elevation/Shape tokens, no gradient spam)"
    // The list items typically don't need heavy elevation to keep it "Clean".
    // I'll use a surface-colored card with low elevation or just a column if I want a list look.
    // The previous design was a Card. Let's upgrade to DailyFocusCard but with Level0 or Level1.
    // Actually, distinct cards for tasks can look cluttered if there are many. 
    // "DailyFocusCard" implies elevation. 
    // Let's use DailyFocusCard but possibly Level0 for a flat look if desired, or Level1 for pop.
    // Let's go with Level1 (subtle) as per plan.

    DailyFocusCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.xxs),
        elevation = Elevation.Level1,
        shape = AppShapes.Medium
    ) {
        Column(modifier = Modifier.padding(Spacing.xs)) {
            // Parent task row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PremiumCheckbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleTask() }
                )
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f)
                )
                if (items.isNotEmpty()) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess
                            else Icons.Default.ExpandMore,
                            contentDescription = "Toggle sub-items",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = { showAddItem = !showAddItem }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add sub-item",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Sub-items
            AnimatedVisibility(visible = expanded && items.isNotEmpty()) {
                Column(modifier = Modifier.padding(start = Spacing.xl)) { // shift Indentation
                    items.forEach { item ->
                        SubTaskItemRow(item = item, onToggle = { onToggleItem(item) })
                    }
                }
            }

            // Add sub-item inline
            AnimatedVisibility(visible = showAddItem) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.xl, top = Spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemTitle,
                        onValueChange = { newItemTitle = it },
                        placeholder = { Text("Sub-item title") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.width(Spacing.xs))
                    TextButton(
                        onClick = {
                            if (newItemTitle.isNotBlank()) {
                                onAddSubItem(task.id, newItemTitle)
                                newItemTitle = ""
                                showAddItem = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
private fun SubTaskItemRow(
    item: TaskItem,
    onToggle: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (item.isCompleted)
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.onSurface,
        label = "subItemTextColor",
        animationSpec = AnimationConstants.fastTween()
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumCheckbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() },
            modifier = Modifier.scale(0.8f) // Smaller checkbox for sub-items
        )
        Spacer(modifier = Modifier.width(Spacing.xs))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
        )
    }
}
