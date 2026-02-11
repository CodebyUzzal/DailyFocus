package com.dailyfocus.presentation.habits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.*
import com.dailyfocus.domain.usecase.habits.HabitCalendarDay
import com.dailyfocus.domain.usecase.habits.HabitDayStatus
import java.time.format.TextStyle
import java.util.Locale

/**
 * A calendar heatmap showing 90 days of habit completion data.
 * Each cell represents one day, colored by status:
 * - Done (teal), Missed (light red), Today (indigo), Future/Inactive (grey)
 */
@Composable
fun CalendarHeatmap(
    days: List<HabitCalendarDay>,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) return

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Last 90 Days",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Group by week (rows of 7)
        val weeks = days.chunked(7)

        // Day-of-week labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach { label ->
                Box(
                    modifier = Modifier.size(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { day ->
                    HeatmapCell(day = day)
                }
                // Pad incomplete weeks
                repeat(7 - week.size) {
                    Spacer(modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
        }

        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LegendItem(color = HeatmapDone, label = "Done")
            LegendItem(color = HeatmapMissed, label = "Missed")
            LegendItem(color = HeatmapToday, label = "Today")
        }
    }
}

@Composable
private fun HeatmapCell(day: HabitCalendarDay) {
    val color = when (day.status) {
        HabitDayStatus.DONE -> HeatmapDone
        HabitDayStatus.MISSED -> HeatmapMissed
        HabitDayStatus.TODAY -> HeatmapToday
        HabitDayStatus.FUTURE -> HeatmapEmpty.copy(alpha = 0.3f)
        HabitDayStatus.INACTIVE -> HeatmapEmpty
    }

    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(shape)
            .background(color.copy(alpha = 0.8f))
            .then(
                if (day.status == HabitDayStatus.TODAY)
                    Modifier.border(2.dp, HeatmapToday, shape)
                else Modifier
            )
    )
}

@Composable
private fun LegendItem(
    color: androidx.compose.ui.graphics.Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
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
