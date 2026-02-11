package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Retrieves all today tasks for a given date, ordered by creation time.
 */
class GetTodayTasksUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    operator fun invoke(date: LocalDate = DateUtils.today()): Flow<List<TodayTask>> {
        return repository.getByDate(date)
    }
}
