package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.DailyTaskInstance
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for per-date [DailyTaskInstance] records. */
interface DailyTaskInstanceRepository {
    fun getByDate(date: LocalDate): Flow<List<DailyTaskInstance>>
    suspend fun getCompletedDatesForTask(recurringTaskId: Long): List<LocalDate>
    suspend fun insertAll(instances: List<DailyTaskInstance>)
    suspend fun toggleCompletion(id: Long, isCompleted: Boolean)
    suspend fun existsForDate(recurringTaskId: Long, date: LocalDate): Boolean
}
