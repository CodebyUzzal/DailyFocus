package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.DailyLogEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyLogEntryDao {

    @Query("SELECT * FROM daily_log_entries ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<DailyLogEntryEntity>>

    @Query("SELECT * FROM daily_log_entries WHERE date = :date ORDER BY createdAt DESC")
    fun getByDate(date: LocalDate): Flow<List<DailyLogEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailyLogEntryEntity): Long

    @Update
    suspend fun update(entry: DailyLogEntryEntity)

    @Query("DELETE FROM daily_log_entries WHERE id = :id")
    suspend fun delete(id: Long)
}
