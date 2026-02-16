package com.dailyfocus.presentation.habits.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.dailyfocus.core.ui.theme.HeatmapDoneSoft
import com.dailyfocus.core.ui.theme.HeatmapMissedSoft
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Premium Calendar Heatmap (Phase 3 & 4).
 *
 * Visual hierarchy:
 * - Card (Surface 2)
 * - Header (Month Label + Legend)
 * - Days Row (M T W T F S S)
 * - Grid (Weeks) with animation
 */
@Composable
fun CalendarHeatmap(
    completedDates: Set<LocalDate>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    // Phase 4: Month Navigation State
    currentMonth: YearMonth = YearMonth.now(),
    onMonthChanged: (YearMonth) -> Unit = {}
) {
    val today = LocalDate.now()
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

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
            // Header with Month Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Month Switcher
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onMonthChanged(currentMonth.minusMonths(1)) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous")
                    }
                    
                    AnimatedContent(
                        targetState = currentMonth,
                        transitionSpec = {
                            (slideInHorizontally { width -> if (targetState.isAfter(initialState)) width else -width } + fadeIn())
                                .togetherWith(slideOutHorizontally { width -> if (targetState.isAfter(initialState)) -width else width } + fadeOut())
                        },
                        label = "MonthTransition"
                    ) { month ->
                        Text(
                            text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    IconButton(
                        onClick = { onMonthChanged(currentMonth.plusMonths(1)) },
                        enabled = !currentMonth.plusMonths(1).isAfter(YearMonth.from(today).plusMonths(1)) // Prevent going too far future? Optional constraint.
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next")
                    }
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

            // Grid Content
            // Calculate weeks for the selected month
            val firstDayOfMonth = currentMonth.atDay(1)
            val dayOfWeekOffset = (firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value + 7) % 7
            val startDate = firstDayOfMonth.minusDays(dayOfWeekOffset.toLong())
            
            val endOfMonth = currentMonth.atEndOfMonth()
            val endDayOffset = (7 - (endOfMonth.dayOfWeek.value - firstDayOfWeek.value + 1) + 7) % 7
            val endDate = endOfMonth.plusDays(endDayOffset.toLong())

            val daysToShow = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1
            val weeks = (0 until daysToShow).map { startDate.plusDays(it) }.chunked(7)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        week.forEach { date ->
                            val isCurrentMonth = date.month == currentMonth.month
                            val isFuture = date.isAfter(today)
                            val isToday = date == today
                            val isDone = date in completedDates
                            // "Missed" logic: Past + Not Done + In Current Month View (to avoid clutter)
                            val isMissed = !isFuture && !isDone && !isToday && isCurrentMonth

                            HeatmapCell(
                                modifier = Modifier.weight(1f),
                                date = date,
                                isToday = isToday,
                                isDone = isDone,
                                isMissed = isMissed,
                                isFuture = isFuture,
                                isCurrentMonth = isCurrentMonth,
                                onClick = { if (!isFuture) onDayClick(date) }
                            )
                        }
                    }
                }
            }

            // Legend at the bottom
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center, // Centered align
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = HeatmapDoneSoft, label = "Done")
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(color = HeatmapMissedSoft, label = "Missed")
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(color = Color.Transparent, label = "Today", isBordered = true)
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
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    // Colors
    val backgroundColor = when {
        isDone -> HeatmapDoneSoft // Premium soft teal
        isMissed -> HeatmapMissedSoft // Premium soft red
        else -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = if (isCurrentMonth) 0.3f else 0.1f) // Empty
    }
    
    val borderColor = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent
    
    val animatedColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(300),
        label = "cellColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isDone) 1f else 0.85f,
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
            .clickable(enabled = !isFuture && isCurrentMonth, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {}
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
        ) {}
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
