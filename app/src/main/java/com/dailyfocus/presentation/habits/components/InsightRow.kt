package com.dailyfocus.presentation.habits.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.Spacing

/**
 * Row of secondary insights: Completion Rate, Weekly Frequency, Best Week.
 */
@Composable
fun InsightRow(
    completionRate: Int,
    currentWeekCount: Int,
    totalLogs: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        InsightItem(
            label = "Completion Rate",
            value = "$completionRate%",
            modifier = Modifier.weight(1f)
        )
        InsightItem(
            label = "This Week",
            value = "$currentWeekCount / 7",
            modifier = Modifier.weight(1f)
        )
        InsightItem(
            label = "Total Days",
            value = "$totalLogs",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InsightItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(vertical = Spacing.s, horizontal = Spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
