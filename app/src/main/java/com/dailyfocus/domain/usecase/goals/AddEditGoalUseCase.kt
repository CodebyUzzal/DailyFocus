package com.dailyfocus.domain.usecase.goals

import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.repository.GoalRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Add or update a [Goal] and optionally manage its [GoalItem]s.
 */
class AddEditGoalUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    /** Creates a new goal and returns its ID. */
    suspend fun addGoal(goal: Goal): Long = repository.insertGoal(goal)

    /** Updates an existing goal. */
    suspend fun updateGoal(goal: Goal) {
        repository.updateGoal(goal.copy(updatedAt = LocalDateTime.now()))
    }

    /** Deletes a goal and its items (cascade handled by Room FK). */
    suspend fun deleteGoal(id: Long) = repository.deleteGoal(id)

    /** Adds a new item to a goal. */
    suspend fun addGoalItem(item: GoalItem): Long = repository.insertGoalItem(item)

    /** Removes an item from a goal. */
    suspend fun deleteGoalItem(id: Long) = repository.deleteGoalItem(id)
}
