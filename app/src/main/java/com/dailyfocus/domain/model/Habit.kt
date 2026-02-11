package com.dailyfocus.domain.model

import java.time.LocalDateTime

/**
 * A habit the user wants to build over time.
 * Habits are NOT tasks — they have their own tracking, streaks, and calendar view.
 *
 * @property id unique identifier
 * @property name display name (e.g. "Meditate")
 * @property frequency how often the habit should be performed
 * @property isActive whether the habit is currently being tracked
 * @property createdAt audit timestamp
 * @property updatedAt audit timestamp
 */
data class Habit(
    val id: Long = 0,
    val name: String,
    val frequency: HabitFrequency = HabitFrequency.Daily,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
