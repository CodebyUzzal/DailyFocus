package com.dailyfocus.domain.usecase.goals

import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.repository.GoalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    /** Progress as a percentage 0.0–100.0 for display. */
    val progressPercent: Float
        get() = if (totalCount > 0) (completedCount.toFloat() / totalCount * 100f) else 0f
}

/**
 * Retrieves all goals with their items and computed progress.
 *
 * BUG FIX: Uses flatMapLatest + combine instead of .first() so the flow
 * re-emits whenever items are added/toggled — fixing the milestone add bug.
 */
class GetGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<GoalWithProgress>> {
        return repository.getAllGoals().flatMapLatest { goals ->
            if (goals.isEmpty()) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                val itemFlows = goals.map { goal ->
                    repository.getItemsByGoal(goal.id)
                }
                combine(itemFlows) { itemArrays ->
                    goals.mapIndexed { index, goal ->
                        val items = itemArrays[index]
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
    }
}
