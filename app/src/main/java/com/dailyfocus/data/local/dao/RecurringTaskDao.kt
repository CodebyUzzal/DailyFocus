package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.RecurringTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTaskDao {

    @Query("SELECT * FROM recurring_tasks WHERE isActive = 1 ORDER BY createdAt ASC")
    fun getAllActive(): Flow<List<RecurringTaskEntity>>

    @Query("SELECT * FROM recurring_tasks ORDER BY createdAt ASC")
    fun getAll(): Flow<List<RecurringTaskEntity>>

    @Query("SELECT * FROM recurring_tasks WHERE id = :id")
    suspend fun getById(id: Long): RecurringTaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: RecurringTaskEntity): Long

    @Update
    suspend fun update(task: RecurringTaskEntity): Int

    @Query("DELETE FROM recurring_tasks WHERE id = :id")
    suspend fun delete(id: Long): Int
}
