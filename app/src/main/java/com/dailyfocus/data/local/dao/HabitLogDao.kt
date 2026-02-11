package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitLogDao {

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY date ASC")
    fun getByHabit(habitId: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT date FROM habit_logs WHERE habitId = :habitId ORDER BY date ASC")
    suspend fun getLogDatesForHabit(habitId: Long): List<LocalDate>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: HabitLogEntity)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun delete(habitId: Long, date: LocalDate)

    @Query("SELECT EXISTS(SELECT 1 FROM habit_logs WHERE habitId = :habitId AND date = :date)")
    suspend fun hasLogForDate(habitId: Long, date: LocalDate): Boolean
}
