package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

import kotlinx.coroutines.flow.combine

/**
 * UI representation of a habit with convenience flags.
 */
data class HabitWithStreak(
    val habit: Habit,
    val currentStreak: Int,
    val longestStreak: Int,
    val completedToday: Boolean,
    val last7Days: List<Boolean> // Boolean for each of the last 7 days (including today)
)

/**
 * Retrieves all habits with their streak data and last 7 days history.
 */
class GetHabitsWithStreaksUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<HabitWithStreak>> {
        val today = DateUtils.today()
        val start = today.minusDays(6)

        return combine(
            repository.getAllHabits(),
            repository.getLogsByDateRange(start, today)
        ) { habits, logs ->
            // logs is List<Pair<HabitId, Date>>
            habits.map { habit ->
                val habitLogs = logs.filter { it.first == habit.id }.map { it.second }.toSet()
                
                // Calculate history for last 7 days (today is last)
                val history = (0..6).map { i ->
                    val date = start.plusDays(i.toLong())
                    habitLogs.contains(date)
                }

                HabitWithStreak(
                    habit = habit,
                    currentStreak = habit.currentStreak,
                    longestStreak = habit.longestStreak,
                    completedToday = habitLogs.contains(today),
                    last7Days = history
                )
            }
        }
    }
}
