package com.dailyfocus.domain.usecase.goals

import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.repository.GoalRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Toggles a [GoalItem]'s completion state within a goal.
 */
class ToggleGoalItemUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(item: GoalItem) {
        repository.updateGoalItem(
            item.copy(
                isCompleted = !item.isCompleted,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
