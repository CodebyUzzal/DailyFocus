package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ToggleTaskItemUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(item: TaskItem, parentTask: TodayTask) {
        repository.updateTaskItem(
            item.copy(
                isCompleted = !item.isCompleted,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
