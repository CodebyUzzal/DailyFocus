package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.DailyLogEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyLogEntryDao {

    @Query("SELECT * FROM focus_logs ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<DailyLogEntryEntity>>

    @Query("SELECT * FROM focus_logs WHERE date = :date ORDER BY createdAt DESC")
    fun getByDate(date: LocalDate): Flow<List<DailyLogEntryEntity>>

    @Query("SELECT * FROM focus_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, createdAt DESC")
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyLogEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailyLogEntryEntity): Long

    @Update
    suspend fun update(entry: DailyLogEntryEntity)

    @Query("DELETE FROM focus_logs WHERE id = :id")
    suspend fun delete(id: Long)
}
