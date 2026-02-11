package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * UI representation of a habit with convenience flags.
 */
data class HabitWithStreak(
    val habit: Habit,
    val currentStreak: Int,
    val longestStreak: Int,
    val completedToday: Boolean
)

/**
 * Retrieves all habits with their streak data.
 * Streaks are read directly from the entity (Option B).
 * Only `completedToday` requires a log check.
 */
class GetHabitsWithStreaksUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<HabitWithStreak>> {
        return repository.getAllHabits().map { habits ->
            val today = DateUtils.today()
            habits.map { habit ->
                HabitWithStreak(
                    habit = habit,
                    currentStreak = habit.currentStreak,
                    longestStreak = habit.longestStreak,
                    completedToday = repository.hasLogForDate(habit.id, today)
                )
            }
        }
    }
}
