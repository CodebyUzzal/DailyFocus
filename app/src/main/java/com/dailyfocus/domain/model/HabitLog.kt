package com.dailyfocus.domain.model

import java.time.LocalDate

/**
 * A single completion log entry for a [Habit] on a specific date.
 * The existence of a [HabitLog] for a date means the habit was done that day.
 *
 * @property id unique identifier
 * @property habitId FK to the parent [Habit]
 * @property date the date the habit was completed
 */
data class HabitLog(
    val id: Long = 0,
    val habitId: Long,
    val date: LocalDate
)
