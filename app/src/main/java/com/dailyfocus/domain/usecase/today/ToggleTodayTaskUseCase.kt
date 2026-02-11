package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import javax.inject.Inject

/**
 * Toggles a [TodayTask]'s completion state.
 */
class ToggleTodayTaskUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(task: TodayTask) {
        repository.update(task.copy(isCompleted = !task.isCompleted))
    }
}
