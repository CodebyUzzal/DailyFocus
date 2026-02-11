package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Retrieves one-time tasks for a given date, optionally filtered by category.
 */
class GetTodayTasksUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    operator fun invoke(
        date: LocalDate,
        categoryFilter: TaskCategory? = null
    ): Flow<List<TodayTask>> {
        return repository.getTasksByDate(date).map { tasks ->
            if (categoryFilter != null) {
                tasks.filter { it.category == categoryFilter }
            } else {
                tasks
            }
        }
    }
}
