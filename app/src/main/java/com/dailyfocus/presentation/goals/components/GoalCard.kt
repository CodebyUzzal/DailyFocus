package com.dailyfocus.presentation.goals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumCheckbox
import com.dailyfocus.core.ui.components.PremiumCircularProgress
import com.dailyfocus.core.ui.theme.AnimationConstants
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.usecase.goals.GoalWithProgress

@Composable
fun GoalCard(
    goalWithProgress: GoalWithProgress,
    onToggleItem: (GoalItem) -> Unit,
    onAddItem: (String) -> Unit,
    onDeleteGoal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goal = goalWithProgress.goal
    var expanded by remember { mutableStateOf(false) } // Default collapsed for cleaner look? Or expanded? existing was true. Let's start collapsed to save space if many goals.
    var showAddItem by remember { mutableStateOf(false) }
    var newItemTitle by remember { mutableStateOf("") }

    DailyFocusCard(
        modifier = modifier.fillMaxWidth(),
        elevation = Elevation.Level1
    ) {
        Column(modifier = Modifier.padding(Spacing.m)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress Ring
                PremiumCircularProgress(
                    progress = goalWithProgress.progressPercent / 100f,
                    size = 48.dp,
                    color = if (goalWithProgress.progressPercent >= 100f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.width(Spacing.m))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        modifier = Modifier.padding(top = Spacing.xxs)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    goal.type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.height(24.dp)
                        )
                        Text(
                            text = "${goalWithProgress.completedCount}/${goalWithProgress.totalCount} completed",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        "Toggle items",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDeleteGoal) {
                    Icon(
                        Icons.Default.Delete,
                        "Delete goal",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Items
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = Spacing.m)) {
                    goalWithProgress.items.forEach { item ->
                        GoalItemRow(item = item, onToggle = { onToggleItem(item) })
                    }

                    // Add item inline
                    if (showAddItem) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing.s),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newItemTitle,
                                onValueChange = { newItemTitle = it },
                                placeholder = { Text("New milestone") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(Spacing.xs))
                            TextButton(onClick = {
                                if (newItemTitle.isNotBlank()) {
                                    onAddItem(newItemTitle)
                                    newItemTitle = ""
                                    showAddItem = false
                                }
                            }) { Text("Add") }
                        }
                    } else {
                        TextButton(
                            onClick = { showAddItem = true },
                            modifier = Modifier.padding(top = Spacing.xs)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(Spacing.xs))
                            Text("Add milestone")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItemRow(item: GoalItem, onToggle: () -> Unit) {
    val textColor by animateColorAsState(
        targetValue = if (item.isCompleted)
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.onSurface,
        label = "goalItemColor",
        animationSpec = AnimationConstants.fastTween()
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumCheckbox(checked = item.isCompleted, onCheckedChange = { onToggle() })
        Spacer(modifier = Modifier.width(Spacing.xs))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
        )
    }
}
