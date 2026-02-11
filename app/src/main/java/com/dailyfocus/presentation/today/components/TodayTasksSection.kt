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
import androidx.compose.ui.unit.dp
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
            text = "Today Tasks",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks for today. Tap + to add one.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
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
        label = "taskTextColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Parent task row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleTask() }
                )
                Spacer(modifier = Modifier.width(4.dp))
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
                            contentDescription = "Toggle sub-items"
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
                Column(modifier = Modifier.padding(start = 32.dp)) {
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
                        .padding(start = 32.dp, top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemTitle,
                        onValueChange = { newItemTitle = it },
                        placeholder = { Text("Sub-item title") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
        label = "subItemTextColor"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
        )
    }
}
