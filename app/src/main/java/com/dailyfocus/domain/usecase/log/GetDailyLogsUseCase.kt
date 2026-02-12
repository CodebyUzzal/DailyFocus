package com.dailyfocus.domain.usecase.log

import com.dailyfocus.domain.model.DailyLogEntry
import com.dailyfocus.domain.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Retrieves all daily log entries, ordered by date descending.
 */
class GetDailyLogsUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    operator fun invoke(): Flow<List<DailyLogEntry>> = repository.getAll()
    
    operator fun invoke(date: java.time.LocalDate): Flow<List<DailyLogEntry>> = repository.getByDate(date)
}
