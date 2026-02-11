package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.TodayTaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TodayTaskDao {

    @Query("SELECT * FROM today_tasks WHERE date = :date ORDER BY position ASC")
    fun getByDate(date: LocalDate): Flow<List<TodayTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TodayTaskEntity): Long

    @Update
    suspend fun update(task: TodayTaskEntity)

    @Query("DELETE FROM today_tasks WHERE id = :id")
    suspend fun delete(id: Long)
}
