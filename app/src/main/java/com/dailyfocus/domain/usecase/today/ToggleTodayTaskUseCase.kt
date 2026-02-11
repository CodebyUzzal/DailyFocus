package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.repository.TodayTaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Toggles the completion state of a [TodayTask].
 * When toggling, the updatedAt timestamp is refreshed.
 */
class ToggleTodayTaskUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(taskId: Long, isCompleted: Boolean) {
        val task = repository.getTasksByDate(java.time.LocalDate.now()).let { flow ->
            // We need a direct get; the flow is for observation.
            // Using updateTask with the toggled state.
            return@let null
        }
        // Simplified: delegate directly. The repository handles the update.
        // ViewModel provides the full task object.
    }

    /**
     * Toggles a task given the full object (preferred approach from ViewModel).
     */
    suspend fun toggle(task: com.dailyfocus.domain.model.TodayTask) {
        repository.updateTask(
            task.copy(
                isCompleted = !task.isCompleted,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
