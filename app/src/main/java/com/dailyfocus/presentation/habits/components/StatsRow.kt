package com.dailyfocus.presentation.habits.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.Spacing

/**
 * Premium Stats Row.
 * Displays key metrics in a clean, elevated layout.
 */
@Composable
fun StatsRow(
    completionRate: Int,
    currentWeekCount: Int,
    totalLogs: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Premium spacing
    ) {
        StatCard(
            label = "Completion",
            value = "$completionRate%",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "This Week",
            value = "$currentWeekCount / 7",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "Total Days",
            value = "$totalLogs",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Value with animation
            AnimatedContent(
                targetState = value,
                label = "statValue",
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn())
                        .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                }
            ) { targetValue ->
               Text(
                    text = targetValue,
                    style = MaterialTheme.typography.headlineSmall, // Value
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Label
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium, // Requested
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
