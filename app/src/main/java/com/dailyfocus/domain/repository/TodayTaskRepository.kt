package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.TodayTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for [TodayTask] entries. */
interface TodayTaskRepository {
    fun getByDate(date: LocalDate): Flow<List<TodayTask>>
    fun getByDateAndCategory(date: LocalDate, category: String): Flow<List<TodayTask>>
    suspend fun insert(task: TodayTask): Long
    suspend fun update(task: TodayTask)
    suspend fun delete(id: Long)
    suspend fun existsForRecurringTaskAndDate(recurringTaskId: Long, date: LocalDate): Boolean
    fun getCompletedByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TodayTask>>
}
