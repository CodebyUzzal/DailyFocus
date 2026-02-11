package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.repository.HabitRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Adds or updates a [Habit].
 */
class AddEditHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        if (habit.id == 0L) {
            repository.insertHabit(habit)
        } else {
            repository.updateHabit(habit.copy(updatedAt = LocalDateTime.now()))
        }
    }
}
