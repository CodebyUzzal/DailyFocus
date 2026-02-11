package com.dailyfocus.domain.usecase.goals

import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * UI representation of a goal with its items and computed progress.
 */
data class GoalWithProgress(
    val goal: Goal,
    val items: List<GoalItem>,
    val completedCount: Int,
    val totalCount: Int
) {
    /** Progress as a fraction 0.0–1.0 for use in LinearProgressIndicator. */
    val progressPercent: Float
        get() = if (totalCount > 0) (completedCount.toFloat() / totalCount * 100f) else 0f
}

/**
 * Retrieves all goals with their items and computed progress.
 */
class GetGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    operator fun invoke(): Flow<List<GoalWithProgress>> {
        return repository.getAllGoals().map { goals ->
            goals.map { goal ->
                val items = repository.getItemsByGoal(goal.id).first()
                GoalWithProgress(
                    goal = goal,
                    items = items,
                    completedCount = items.count { it.isCompleted },
                    totalCount = items.size
                )
            }
        }
    }
}
