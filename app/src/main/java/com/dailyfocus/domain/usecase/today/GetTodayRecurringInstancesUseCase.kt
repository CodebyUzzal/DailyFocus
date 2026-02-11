package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.DailyTaskInstance
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.repository.DailyTaskInstanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Retrieves today's recurring task instances, optionally filtered by category.
 */
class GetTodayRecurringInstancesUseCase @Inject constructor(
    private val repository: DailyTaskInstanceRepository
) {
    operator fun invoke(
        date: LocalDate,
        categoryFilter: TaskCategory? = null
    ): Flow<List<DailyTaskInstance>> {
        return repository.getByDate(date).map { instances ->
            if (categoryFilter != null) {
                instances.filter { it.category == categoryFilter }
            } else {
                instances
            }
        }
    }
}
