package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.repository.HabitRepository
import javax.inject.Inject

/**
 * Creates or updates a habit.
 */
class AddEditHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(
        existingId: Long? = null,
        title: String,
        category: TaskCategory? = null
    ): Long {
        return if (existingId != null && existingId > 0) {
            val existing = repository.getHabitById(existingId) ?: throw IllegalArgumentException("Habit $existingId not found")
            repository.updateHabit(existing.copy(title = title, category = category))
            existingId
        } else {
            repository.insertHabit(Habit(title = title, category = category))
        }
    }
}
