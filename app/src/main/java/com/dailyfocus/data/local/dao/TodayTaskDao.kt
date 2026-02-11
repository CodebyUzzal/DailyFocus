package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.TodayTaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TodayTaskDao {

    @Query("SELECT * FROM today_tasks WHERE date = :date ORDER BY createdAt ASC")
    fun getByDate(date: LocalDate): Flow<List<TodayTaskEntity>>

    @Query("SELECT * FROM today_tasks WHERE date = :date AND category = :category ORDER BY createdAt ASC")
    fun getByDateAndCategory(date: LocalDate, category: String): Flow<List<TodayTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TodayTaskEntity): Long

    @Update
    suspend fun update(task: TodayTaskEntity)

    @Query("DELETE FROM today_tasks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM today_tasks WHERE recurringTaskId = :recurringTaskId AND date = :date)")
    suspend fun existsForRecurringTaskAndDate(recurringTaskId: Long, date: LocalDate): Boolean

    @Query("SELECT * FROM today_tasks WHERE isCompleted = 1 AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getCompletedByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TodayTaskEntity>>
}
