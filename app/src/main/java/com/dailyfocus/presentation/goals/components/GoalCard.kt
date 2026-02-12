package com.dailyfocus.presentation.goals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
    var expanded by remember { mutableStateOf(false) }
    var showAddItem by remember { mutableStateOf(false) }
    var newItemTitle by remember { mutableStateOf("") }

    DailyFocusCard(
        modifier = modifier.fillMaxWidth(),
        elevation = Elevation.Level1
    ) {
        Column(modifier = Modifier.padding(Spacing.m)) {
            // ── Header Area ─────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress Indicator
                Box(contentAlignment = Alignment.Center) {
                    PremiumCircularProgress(
                        progress = goalWithProgress.progressPercent / 100f,
                        size = 56.dp, // Slightly larger
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "${goalWithProgress.progressPercent.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(Spacing.m))

                // Title & Metadata
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        // Type Badge
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.height(20.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 6.dp)) {
                                Text(
                                    text = goal.type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Deadline (if exists)
                        goal.deadline?.let { deadline ->
                            Text("•", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = deadline.format(java.time.format.DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.SHORT)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle items",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // ── Milestones Section ──────────────────────────────────────────
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = Spacing.m)) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(Spacing.s))

                    // Active Items
                    val activeItems = goalWithProgress.items.filter { !it.isCompleted }
                    if (activeItems.isNotEmpty()) {
                        Text(
                            text = "Active Milestones",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = Spacing.xs)
                        )
                        activeItems.forEach { item ->
                            GoalItemRow(item = item, onToggle = { onToggleItem(item) })
                        }
                        Spacer(modifier = Modifier.height(Spacing.m))
                    }

                    // Completed Items
                    val completedItems = goalWithProgress.items.filter { it.isCompleted }
                    if (completedItems.isNotEmpty()) {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = Spacing.xs)
                        )
                        completedItems.forEach { item ->
                            GoalItemRow(item = item, onToggle = { onToggleItem(item) })
                        }
                         Spacer(modifier = Modifier.height(Spacing.m))
                    }

                    // Add New Item Input
                    if (showAddItem) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = newItemTitle,
                                onValueChange = { newItemTitle = it },
                                placeholder = { Text("New milestone") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = {
                                if (newItemTitle.isNotBlank()) {
                                    onAddItem(newItemTitle)
                                    newItemTitle = ""
                                    // Keep input open for rapid entry
                                }
                            }) {
                                Icon(Icons.Default.Add, "Add", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { showAddItem = false }) {
                                Icon(Icons.Default.Close, "Cancel", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        TextButton(
                            onClick = { showAddItem = true },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Milestone")
                        }
                    }
                    
                    // Delete Goal (moved to bottom of expanded for safety)
                    Spacer(modifier = Modifier.height(Spacing.m))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = onDeleteGoal,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                             Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                             Spacer(modifier = Modifier.width(4.dp))
                             Text("Delete Goal")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItemRow(item: GoalItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
            color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
        )
    }
}
