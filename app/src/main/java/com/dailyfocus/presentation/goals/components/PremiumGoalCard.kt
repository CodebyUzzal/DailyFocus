package com.dailyfocus.presentation.goals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.components.DailyFocusCard
import com.dailyfocus.core.ui.components.PremiumCircularProgress
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.usecase.goals.GoalWithProgress
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PremiumGoalCard(
    goalWithProgress: GoalWithProgress,
    onToggleItem: (GoalItem) -> Unit,
    onAddItem: (String) -> Unit,
    onDeleteGoal: () -> Unit, // Now accessed via overflow menu
    modifier: Modifier = Modifier
) {
    val goal = goalWithProgress.goal
    var expanded by remember { mutableStateOf(false) }
    var showAddItem by remember { mutableStateOf(false) }
    var newItemTitle by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    // Progress Animation
    val progressAnimated by animateFloatAsState(
        targetValue = goalWithProgress.progressPercent / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "Progress Animation"
    )

    // Completion State styling
    val isCompleted = goalWithProgress.progressPercent >= 100
    val cardContainerColor = if (isCompleted) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level2) // Slightly brighter/tonal
    } else {
        MaterialTheme.colorScheme.surface // Surface 2 equivalent in Material3 logic often uses surfaceColorAtElevation or surfaceVariant
    }

    // Emotional Feedback Colors
    val progressColor by animateColorAsState(
        targetValue = when {
            goalWithProgress.progressPercent >= 100 -> MaterialTheme.colorScheme.primary // Completion
            goalWithProgress.progressPercent >= 75 -> MaterialTheme.colorScheme.tertiary // High progress (Intensity increase)
            goalWithProgress.progressPercent >= 50 -> MaterialTheme.colorScheme.secondary // Mid progress
            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) // Early progress
        },
        label = "Progress Color"
    )

    DailyFocusCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = if (isCompleted) Elevation.Level2 else Elevation.Level1, 
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(Spacing.m)) {
            // ── Top Section: Progress + Title ─────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Large Animated Circular Progress
                Box(contentAlignment = Alignment.Center) {
                    PremiumCircularProgress(
                        progress = progressAnimated,
                        size = 80.dp, 
                        strokeWidth = 6.dp,
                        color = progressColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progressAnimated * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface 
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Spacing.m))

                // Right: Title & Details
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        // Overflow Menu
                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(24.dp).offset(x = 8.dp, y = (-8).dp) // Tweak alignment
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete Goal") },
                                    onClick = { 
                                        onDeleteGoal()
                                        showMenu = false 
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = null)
                                    },
                                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.error, leadingIconColor = MaterialTheme.colorScheme.error)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.xs))

                    // Chips / Metadata Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(goal.type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = null,
                            modifier = Modifier.height(24.dp)
                        )

                        goal.deadline?.let { deadline ->
                            SuggestionChip(
                                onClick = {},
                                label = { 
                                    Text(deadline.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))) 
                                },
                                icon = {
                                    Icon(Icons.Default.DateRange, null, modifier = Modifier.size(12.dp))
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                border = null,
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(Spacing.s))
                    
                    // Milestone Summary
                    val completedCount = goalWithProgress.items.count { it.isCompleted }
                    val totalCount = goalWithProgress.items.size
                    Text(
                        text = "$completedCount of $totalCount milestones completed",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Secondary linear progress for clarity
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = progressAnimated,
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = progressColor.copy(alpha = 0.8f),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            // ── Milestones List ──────────────────────────────────────────────
            // Always show first few or collapsed view? 
            // The requirement says "Expandable" wasn't explicitly forbidden but previous card had it. 
            // "Right Section... Milestone summary" suggests compact view.
            // Let's keep it expandable but cleaner.
            
            Spacer(modifier = Modifier.height(Spacing.m))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(Spacing.m))

            // Active Milestones
            val activeItems = goalWithProgress.items.filter { !it.isCompleted }
            if (activeItems.isNotEmpty()) {
                activeItems.forEach { item ->
                    MilestoneItem(item = item, onToggle = { onToggleItem(item) })
                }
            } else if (goalWithProgress.items.isNotEmpty() && !isCompleted) {
                 // All items completed but goal not marked? Use logic to handle
            }

            // Completed Milestones (Optional: Hide if too many, or show at bottom)
            val completedItems = goalWithProgress.items.filter { it.isCompleted }
            if (completedItems.isNotEmpty()) {
                 if (activeItems.isNotEmpty()) {
                     Spacer(modifier = Modifier.height(Spacing.s))
                     Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                     )
                 }
                 completedItems.forEach { item ->
                    MilestoneItem(item = item, onToggle = { onToggleItem(item) })
                }
            }

            Spacer(modifier = Modifier.height(Spacing.m))

            // Add Milestone Input
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    IconButton(onClick = {
                        if (newItemTitle.isNotBlank()) {
                            onAddItem(newItemTitle)
                            newItemTitle = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, "Save", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showAddItem = false }) {
                        Icon(Icons.Default.Close, "Cancel", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                TextButton(
                    onClick = { showAddItem = true },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Milestone")
                }
            }
        }
    }
}


