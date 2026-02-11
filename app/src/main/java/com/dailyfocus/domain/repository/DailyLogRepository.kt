package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.DailyLogEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for focus log entries. */
interface DailyLogRepository {
    fun getAll(): Flow<List<DailyLogEntry>>
    fun getByDate(date: LocalDate): Flow<List<DailyLogEntry>>
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyLogEntry>>
    suspend fun insert(entry: DailyLogEntry): Long
    suspend fun update(entry: DailyLogEntry)
    suspend fun delete(id: Long)
}
