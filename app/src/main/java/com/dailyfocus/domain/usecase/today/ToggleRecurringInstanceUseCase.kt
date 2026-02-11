package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.repository.DailyTaskInstanceRepository
import javax.inject.Inject

/**
 * Toggles the completion state of a [DailyTaskInstance].
 */
class ToggleRecurringInstanceUseCase @Inject constructor(
    private val repository: DailyTaskInstanceRepository
) {
    suspend operator fun invoke(instanceId: Long, isCompleted: Boolean) {
        repository.toggleCompletion(instanceId, isCompleted)
    }
}
