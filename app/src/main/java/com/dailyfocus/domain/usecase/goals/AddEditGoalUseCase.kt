package com.dailyfocus.domain.usecase.goals

import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.model.GoalType
import com.dailyfocus.domain.repository.GoalRepository
import javax.inject.Inject

/**
 * Creates or updates a goal, and can also add items (milestones) to a goal.
 */
class AddEditGoalUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend fun addGoal(
        title: String,
        type: GoalType = GoalType.INFINITE,
        deadline: java.time.LocalDate? = null
    ): Long {
        return repository.insertGoal(Goal(title = title, type = type, deadline = deadline))
    }

    suspend fun updateGoal(goal: Goal) {
        repository.updateGoal(goal)
    }

    suspend fun deleteGoal(id: Long) {
        repository.deleteGoal(id)
    }

    suspend fun addItem(goalId: Long, title: String): Long {
        return repository.insertGoalItem(
            GoalItem(goalId = goalId, title = title)
        )
    }

    suspend fun deleteItem(id: Long) {
        repository.deleteGoalItem(id)
    }
}
