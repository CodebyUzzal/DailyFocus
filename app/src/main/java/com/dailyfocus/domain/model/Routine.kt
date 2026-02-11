package com.dailyfocus.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * A routine task that generates [DailyTaskInstance] entries each day.
 * This is the _definition_, not the daily completion record.
 *
 * @property id unique identifier
 * @property title display name (e.g. "Drink water")
 * @property category personal or office classification
 * @property currentStreak current consecutive days completed
 * @property longestStreak longest consecutive days completed
 * @property lastCompletedDate date when this routine was last completed
 * @property reminderTime optional reminder label (no notification logic in v1)
 * @property isActive whether this routine generates daily instances
 * @property position stable ordering index
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class Routine(
    val id: Long = 0,
    val title: String,
    val category: TaskCategory,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletedDate: LocalDate? = null,
    val reminderTime: String? = null,
    val isActive: Boolean = true,
    val position: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
