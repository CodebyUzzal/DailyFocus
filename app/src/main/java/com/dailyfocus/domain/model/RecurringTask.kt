package com.dailyfocus.domain.model

import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * A recurring task that auto-generates [TodayTask] entries when the current
 * day matches one of the configured [daysOfWeek].
 *
 * This is the _definition_ — not a daily completion record.
 *
 * @property id unique identifier
 * @property title display name (e.g. "Morning standup")
 * @property category Personal or Office classification
 * @property daysOfWeek which days this task should generate (empty = every day)
 * @property isActive whether this task generates daily instances
 * @property createdAt audit timestamp
 */
data class RecurringTask(
    val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
