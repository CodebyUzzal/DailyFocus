package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.repository.TodayTaskRepository
import javax.inject.Inject

class DeleteTaskItemUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTaskItem(id)
    }
}
