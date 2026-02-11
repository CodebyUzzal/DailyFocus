package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.repository.TodayTaskRepository
import javax.inject.Inject

class AddTaskItemUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(item: TaskItem): Long {
        return repository.insertTaskItem(item)
    }
}
