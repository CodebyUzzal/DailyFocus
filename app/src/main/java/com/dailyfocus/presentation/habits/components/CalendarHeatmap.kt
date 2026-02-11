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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.*
import java.time.LocalDate

/**
 * A calendar heatmap showing 90 days of habit completion data.
 * Each cell represents one day, colored by status:
 * - Done (teal), Today (indigo), Missed/Empty (grey)
 */
@Composable
fun CalendarHeatmap(
    completedDates: Set<LocalDate>,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val startDate = today.minusDays(89) // 90 days including today
    val days = (0L until 90L).map { startDate.plusDays(it) }

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
                week.forEach { date ->
                    HeatmapCell(
                        date = date,
                        isCompleted = date in completedDates,
                        isToday = date == today,
                        isFuture = date.isAfter(today)
                    )
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
private fun HeatmapCell(
    date: LocalDate,
    isCompleted: Boolean,
    isToday: Boolean,
    isFuture: Boolean
) {
    val color = when {
        isToday -> HeatmapToday
        isCompleted -> HeatmapDone
        isFuture -> HeatmapEmpty.copy(alpha = 0.3f)
        else -> HeatmapMissed.copy(alpha = 0.4f)
    }

    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(shape)
            .background(color.copy(alpha = 0.8f))
            .then(
                if (isToday)
                    Modifier.border(2.dp, HeatmapToday, shape)
                else Modifier
            )
    )
}

@Composable
private fun LegendItem(
    color: Color,
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
