package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.model.HabitFrequency
import com.dailyfocus.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * UI representation of a habit with computed streak information.
 */
data class HabitWithStreak(
    val habit: Habit,
    val currentStreak: Int,
    val longestStreak: Int,
    val completedToday: Boolean
)

/**
 * Retrieves all habits with their computed current and longest streaks.
 * Streak logic lives in the domain layer (not SQL) for testability.
 */
class GetHabitsWithStreaksUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<HabitWithStreak>> {
        return repository.getAllHabits().map { habits ->
            habits.map { habit ->
                val logDates = repository.getLogDatesForHabit(habit.id)
                val today = DateUtils.today()
                HabitWithStreak(
                    habit = habit,
                    currentStreak = DateUtils.computeStreak(logDates, today),
                    longestStreak = DateUtils.computeLongestStreak(logDates),
                    completedToday = logDates.contains(today)
                )
            }
        }
    }
}
