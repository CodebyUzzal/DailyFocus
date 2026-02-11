package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.repository.TodayTaskRepository
import javax.inject.Inject

class GetTaskItemsByParentUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(parentTaskId: Long): List<TaskItem> {
        return repository.getTaskItemsByParentOnce(parentTaskId)
    }
}
