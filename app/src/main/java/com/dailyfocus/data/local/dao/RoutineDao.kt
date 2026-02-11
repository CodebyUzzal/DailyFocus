package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.RoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM recurring_tasks WHERE isActive = 1 ORDER BY position ASC")
    fun getAllActive(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM recurring_tasks ORDER BY position ASC")
    fun getAll(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM recurring_tasks WHERE id = :id")
    suspend fun getById(id: Long): RoutineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity): Long

    @Update
    suspend fun update(routine: RoutineEntity): Int

    @Query("DELETE FROM recurring_tasks WHERE id = :id")
    suspend fun delete(id: Long): Int
}
