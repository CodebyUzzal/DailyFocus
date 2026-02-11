package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.repository.RoutineRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Updates the streak for a [Routine] when a daily instance is completed.
 *
 * Logic:
 * - If completed today and last completion was yesterday -> streak++
 * - If completed today and last completion was today -> no change (already counted)
 * - If completed today but last completion was before yesterday -> streak = 1 (reset)
 * - If uncompleting (toggle off) -> This is complex, simplified to no-op or decrement for now.
 *   Ideally, we'd need history, but for v1 we just track forward progress.
 *   For simplicity and safety in v1, we only increment on completion.
 */
class UpdateRoutineStreakUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend operator fun invoke(routineId: Long, date: LocalDate, isCompleted: Boolean) {
        if (!isCompleted) return // We don't handle streak rollback in v1 for simplicity

        val routine = repository.getById(routineId) ?: return

        // If explicitly completing for a past date, we might not update streak unless it connects.
        // For "Today" usage, date is usually today.
        
        val today = DateUtils.today()
        if (date != today) return // Only track streaks for today's actions for now

        val lastDate = routine.lastCompletedDate
        var newStreak = routine.currentStreak

        if (lastDate == today) {
            // Already counted for today
            return
        } else if (lastDate == today.minusDays(1)) {
            // Continued streak
            newStreak += 1
        } else {
            // Broken streak or first time
            newStreak = 1
        }

        val newLongest = maxOf(routine.longestStreak, newStreak)

        repository.update(
            routine.copy(
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastCompletedDate = today
            )
        )
    }
}
