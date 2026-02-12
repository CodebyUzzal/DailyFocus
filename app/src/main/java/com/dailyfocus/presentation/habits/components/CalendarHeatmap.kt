package com.dailyfocus.presentation.habits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Premium Calendar Heatmap.
 * Features:
 * - Chronological Grid (Oldest -> Newest)
 * - Tonal Intensity Scale (Simulated via opacity for now, extensible)
 * - Month Indicators
 * - Premium Visuals
 */
@Composable
fun CalendarHeatmap(
    completedDates: Set<LocalDate>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    // Show last 14 weeks (approx 98 days) to ensure full rows
    val daysToShow = 7 * 14
    val startDate = today.minusDays((daysToShow - 1).toLong())
    
    // Group days by week
    val weeks = remember(completedDates) {
        (0 until daysToShow).map { startDate.plusDays(it.toLong()) }.chunked(7)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface, // Surface 2 via tonal elevation
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Consistency",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Premium Legend
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     LegendDot(color = MaterialTheme.colorScheme.surfaceVariant, label = "Missed")
                     LegendDot(color = MaterialTheme.colorScheme.primary, label = "Done")
                }
            }
    
            // Days Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(28.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
    
            // Grid
            weeks.forEachIndexed { index, week ->
                // Check if this week starts a new month
                val firstDay = week.first()
                val showMonthLabel = firstDay.dayOfMonth <= 7
                
                if (showMonthLabel && index > 0) {
                    Text(
                        text = firstDay.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
    
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { date ->
                        HeatmapCell(
                            date = date,
                            status = when {
                                date == today && date in completedDates -> HeatmapStatus.TODAY_DONE
                                date == today -> HeatmapStatus.TODAY_EMPTY
                                date in completedDates -> HeatmapStatus.DONE
                                else -> HeatmapStatus.EMPTY
                            },
                            onClick = { if (!date.isAfter(today)) onDayClick(date) }
                        )
                    }
                }
            }
        }
    }
}

private enum class HeatmapStatus {
    EMPTY, DONE, TODAY_EMPTY, TODAY_DONE
}

@Composable
private fun HeatmapCell(
    date: LocalDate,
    status: HeatmapStatus,
    onClick: () -> Unit
) {
    val color = when (status) {
        HeatmapStatus.DONE -> MaterialTheme.colorScheme.primary
        HeatmapStatus.TODAY_DONE -> MaterialTheme.colorScheme.primary
        HeatmapStatus.TODAY_EMPTY -> Color.Transparent
        HeatmapStatus.EMPTY -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val borderColor = when (status) {
        HeatmapStatus.TODAY_EMPTY, HeatmapStatus.TODAY_DONE -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    // Animation state
    val isDone = status == HeatmapStatus.DONE || status == HeatmapStatus.TODAY_DONE
    val scale = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isDone) 1f else 0.85f,
        label = "scale"
    )
    val alpha = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isDone) 1f else 0.5f,
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .padding(2.dp) // Gap
            .scale(scale.value)
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = if(status == HeatmapStatus.EMPTY) 0.3f else 1f))
            .border(
                width = if (status == HeatmapStatus.TODAY_EMPTY || status == HeatmapStatus.TODAY_DONE) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
