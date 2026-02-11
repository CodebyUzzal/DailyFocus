package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.DailyLogEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for [DailyLogEntry] time/activity logs. */
interface DailyLogRepository {
    fun getAll(): Flow<List<DailyLogEntry>>
    fun getByDate(date: LocalDate): Flow<List<DailyLogEntry>>
    suspend fun insert(entry: DailyLogEntry): Long
    suspend fun update(entry: DailyLogEntry)
    suspend fun delete(id: Long)
}
