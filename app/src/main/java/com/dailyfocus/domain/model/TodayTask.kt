package com.dailyfocus.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * A one-time task that exists only for a specific day.
 * Unlike [RecurringTask], this does NOT reappear the next day.
 * Supports one level of nesting via [TaskItem] children.
 *
 * @property id unique identifier
 * @property title display name
 * @property category personal or office classification
 * @property date the date this task belongs to
 * @property isCompleted auto-set to true when all children are completed (or manually toggled if no children)
 * @property position stable ordering index within the day
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class TodayTask(
    val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val position: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
