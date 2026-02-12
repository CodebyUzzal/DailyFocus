package com.dailyfocus.presentation.habits.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Premium Calendar Heatmap.
 *
 * Visual hierarchy:
 * - Card (Surface 2)
 * - Header (Month Label + Legend)
 * - Days Row (M T W T F S S)
 * - Grid (Weeks)
 */
@Composable
fun CalendarHeatmap(
    completedDates: Set<LocalDate>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    // Show 3 months including current
    val currentMonth = YearMonth.from(today)
    val startMonth = currentMonth.minusMonths(2)
    
    // We want to show full weeks for these months
    val firstDayOfStartMonth = startMonth.atDay(1)
    // Adjust to start on Monday (or locale specific, checking logic)
    // Using ISO-8601 (Monday start) for consistency with "M T W..." labels
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    val dayOfWeekOffset = (firstDayOfStartMonth.dayOfWeek.value - firstDayOfWeek.value + 7) % 7
    val startDate = firstDayOfStartMonth.minusDays(dayOfWeekOffset.toLong())

    // Calculate end date (end of current month + rest of week)
    val endOfMonth = currentMonth.atEndOfMonth()
    val endDayOffset = (7 - (endOfMonth.dayOfWeek.value - firstDayOfWeek.value + 1) + 7) % 7
    val endDate = endOfMonth.plusDays(endDayOffset.toLong())

    val daysToShow = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1
    val weeks = (0 until daysToShow).map { startDate.plusDays(it) }.chunked(7)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Consistency", // Changed from "History" to match "Header" requirement in Phase 5
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Legend
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LegendItem(color = MaterialTheme.colorScheme.primary, label = "Done")
                    LegendItem(color = MaterialTheme.colorScheme.errorContainer, label = "Missed")
                    LegendItem(color = MaterialTheme.colorScheme.surfaceContainerHigh, label = "Today", isBordered = true)
                }
            }

            // Days Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                weeks.forEachIndexed { index, week ->
                    // Check for month label
                    val firstDay = week.first()
                    // Show month label if week contains the 1st of a month
                    val monthLabel = week.firstOrNull { it.dayOfMonth == 1 }?.month
                        ?.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    
                    if (monthLabel != null) {
                         Text(
                            text = monthLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        week.forEach { date ->
                            // Interaction logic
                            val isFuture = date.isAfter(today)
                            val isToday = date == today
                            val isDone = date in completedDates
                            // "Missed" logic: Past + Not Done + (For simplicity, assuming daily habit)
                            // In real app, check frequency. Here we assume daily.
                            val isMissed = !isFuture && !isDone && !isToday

                            HeatmapCell(
                                modifier = Modifier.weight(1f),
                                date = date,
                                isToday = isToday,
                                isDone = isDone,
                                isMissed = isMissed,
                                isFuture = isFuture,
                                onClick = { if (!isFuture) onDayClick(date) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeatmapCell(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isDone: Boolean,
    isMissed: Boolean,
    isFuture: Boolean,
    onClick: () -> Unit
) {
    // Colors
    val backgroundColor = when {
        isDone -> MaterialTheme.colorScheme.primary
        isMissed -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) // Muted red
        isToday -> Color.Transparent // Border only if empty
        else -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f) // Empty/Future
    }
    
    val borderColor = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent
    
    val animatedColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(300),
        label = "cellColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isDone) 1f else 0.9f,
        label = "cellScale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f) // Square cells
            .padding(2.dp)
            .scale(scale)
            .clip(RoundedCornerShape(6.dp))
            .background(animatedColor)
            .border(
                width = if (isToday) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(enabled = !isFuture, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
         if (isToday && !isDone) {
            // Optional: Small dot or something to indicate "Today" inside? 
            // The border is enough as per requirement.
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, isBordered: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isBordered) Color.Transparent else color)
                .then(
                    if (isBordered) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape) 
                    else Modifier
                )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
