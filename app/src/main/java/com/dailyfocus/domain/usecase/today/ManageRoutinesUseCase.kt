package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.RecurringTask
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.repository.RecurringTaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * CRUD operations for recurring task definitions.
 */
class ManageRecurringTasksUseCase @Inject constructor(
    private val repository: RecurringTaskRepository
) {
    fun getAll(): Flow<List<RecurringTask>> = repository.getAll()
    fun getAllActive(): Flow<List<RecurringTask>> = repository.getAllActive()

    suspend fun add(
        title: String,
        category: TaskCategory,
        daysOfWeek: Set<DayOfWeek> = emptySet()
    ): Long {
        return repository.insert(
            RecurringTask(
                title = title,
                category = category,
                daysOfWeek = daysOfWeek
            )
        )
    }

    suspend fun update(task: RecurringTask) = repository.update(task)
    suspend fun delete(id: Long) = repository.delete(id)
    suspend fun toggleActive(task: RecurringTask) =
        repository.update(task.copy(isActive = !task.isActive))
}
