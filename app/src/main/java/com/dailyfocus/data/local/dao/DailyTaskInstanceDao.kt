package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.DailyTaskInstanceEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyTaskInstanceDao {

    @Query("SELECT * FROM daily_task_instances WHERE date = :date ORDER BY id ASC")
    fun getByDate(date: LocalDate): Flow<List<DailyTaskInstanceEntity>>

    @Query("SELECT date FROM daily_task_instances WHERE recurringTaskId = :taskId AND isCompleted = 1 ORDER BY date ASC")
    suspend fun getCompletedDatesForTask(taskId: Long): List<LocalDate>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(instances: List<DailyTaskInstanceEntity>)

    @Query("UPDATE daily_task_instances SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun toggleCompletion(id: Long, isCompleted: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM daily_task_instances WHERE recurringTaskId = :taskId AND date = :date)")
    suspend fun existsForDate(taskId: Long, date: LocalDate): Boolean
}
