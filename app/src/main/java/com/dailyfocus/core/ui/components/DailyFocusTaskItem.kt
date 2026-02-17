package com.dailyfocus.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.border
import com.dailyfocus.core.ui.theme.Elevation
import com.dailyfocus.core.ui.theme.Radius
import com.dailyfocus.core.ui.theme.Spacing
import com.dailyfocus.domain.model.TaskCategory

import com.dailyfocus.core.ui.theme.StreakRingSuccess

/**
 * Premium Task Item Row.
 * Features:
 * - Animated Checkbox (Scale + Color)
 * - Animated Strike-through
 * - Elevation drop on completion
 * - Tonal background shift
 */
@Composable
fun DailyFocusTaskItem(
    title: String,
    isCompleted: Boolean,
    category: TaskCategory?,
    isRecurring: Boolean = false,
    note: String? = null,
    showCategory: Boolean = true,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation States
    val elevation by animateDpAsState(
        targetValue = if (isCompleted) 0.dp else 2.dp, // Level 0 vs Level 2 (Surface 2)
        label = "elevation"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isCompleted) 
            StreakRingSuccess.copy(alpha = 0.1f) // Motivating light green tint
        else 
            MaterialTheme.colorScheme.surface, // Surface with tonal elevation will create Surface2 look
        label = "background"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isCompleted) 0.8f else 1.0f,
        label = "alpha"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1.1f else 1.0f,
        label = "scale"
    )

    Surface(
        onClick = onToggle,
        enabled = !isCompleted, // Only clickable when active
        modifier = modifier
            .fillMaxWidth()
            .alpha(contentAlpha),
        shape = RoundedCornerShape(16.dp), // Premium roundness
        color = backgroundColor,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = Spacing.m, horizontal = Spacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            // Animated Checkbox
            CircularCheckbox(
                checked = isCompleted,
                onToggle = onToggle,
                modifier = Modifier.scale(checkScale)
            )

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Secondary Row: Note + Metadata
                if (!note.isNullOrBlank() || isRecurring || (category != null && showCategory)) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         if (isRecurring) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Recurring",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }

                        if (!note.isNullOrBlank()) {
                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                        
                        // Category Tag (only if enabled)
                        if (category != null && showCategory) {
                             // Add separator if we have other content
                             if (!note.isNullOrBlank() || isRecurring) {
                                 Text("•", style = MaterialTheme.typography.bodySmall)
                             }
                             
                            Text(
                                text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = when(category) {
                                    TaskCategory.OFFICE -> MaterialTheme.colorScheme.primary
                                    TaskCategory.PERSONAL -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.tertiary
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CircularCheckbox(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
         targetValue = if (checked) MaterialTheme.colorScheme.primary else Color.Transparent,
         label = "checkboxBg"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        label = "checkboxBorder"
    )

    Box(
        modifier = modifier
            .size(24.dp)
            .background(backgroundColor, CircleShape)
            .then(
                if (!checked) Modifier.border(2.dp, borderColor, CircleShape) else Modifier
            )
            .clickable(onClick = onToggle), // Explicit click target for checkbox
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

