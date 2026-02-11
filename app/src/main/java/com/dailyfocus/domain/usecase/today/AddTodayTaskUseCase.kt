package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import javax.inject.Inject

/**
 * Adds a new one-time task for today.
 */
class AddTodayTaskUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(task: TodayTask): Long {
        return repository.insertTask(task)
    }
}
