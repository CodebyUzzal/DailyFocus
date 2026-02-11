package com.dailyfocus.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * A habit the user wants to build over time.
 * Habits have their own streak tracking — separate from recurring tasks.
 *
 * Streak is stored on the entity (Option B) and updated on toggle.
 * HabitLog entries exist for historical display only, NOT for streak computation.
 *
 * @property id unique identifier
 * @property title display name (e.g. "Meditate")
 * @property category optional Personal/Office tag
 * @property currentStreak current consecutive days completed
 * @property longestStreak best streak ever achieved
 * @property lastCompletedDate date when habit was last marked done
 * @property createdAt audit timestamp
 */
data class Habit(
    val id: Long = 0,
    val title: String,
    val category: TaskCategory? = null,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletedDate: LocalDate? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
