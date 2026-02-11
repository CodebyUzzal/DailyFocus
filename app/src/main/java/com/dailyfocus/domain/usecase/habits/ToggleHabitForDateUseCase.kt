package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.repository.HabitRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Toggles a habit's completion for a specific date.
 *
 * Uses **Option B** for streaks: streak values are stored on the HabitEntity
 * and updated immediately on toggle, rather than computed from logs.
 *
 * When marking complete:
 *   - If already completed today → removes log, decrements streak
 *   - If not completed → adds log, updates streak + lastCompletedDate
 *
 * Prevents double-completion for the same day.
 */
class ToggleHabitForDateUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long, date: LocalDate = DateUtils.today()) {
        val habit = repository.getHabitById(habitId) ?: return
        val hasLog = repository.hasLogForDate(habitId, date)

        if (hasLog) {
            // Undo completion
            repository.deleteLog(habitId, date)

            // Recalculate streak: if this was the latest date, decrement
            val newStreak = if (habit.lastCompletedDate == date) {
                maxOf(0, habit.currentStreak - 1)
            } else {
                habit.currentStreak
            }
            val newLastCompleted = if (habit.lastCompletedDate == date) {
                // Find previous log date
                val logDates = repository.getLogDatesForHabit(habitId)
                logDates.lastOrNull()
            } else {
                habit.lastCompletedDate
            }
            repository.updateHabit(
                habit.copy(
                    currentStreak = newStreak,
                    lastCompletedDate = newLastCompleted
                )
            )
        } else {
            // Mark complete
            repository.insertLog(habitId, date)

            val newStreak: Int
            val newLongest: Int

            if (habit.lastCompletedDate != null) {
                val daysSinceLast = ChronoUnit.DAYS.between(habit.lastCompletedDate, date)
                newStreak = when {
                    daysSinceLast == 1L -> habit.currentStreak + 1 // consecutive
                    daysSinceLast == 0L -> habit.currentStreak      // same day (shouldn't reach here)
                    else -> 1 // streak broken, start fresh
                }
            } else {
                newStreak = 1 // first ever completion
            }
            newLongest = maxOf(habit.longestStreak, newStreak)

            repository.updateHabit(
                habit.copy(
                    currentStreak = newStreak,
                    longestStreak = newLongest,
                    lastCompletedDate = date
                )
            )
        }
    }
}
