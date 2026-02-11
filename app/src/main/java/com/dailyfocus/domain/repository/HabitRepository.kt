package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.model.HabitLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for [Habit]s and their [HabitLog] entries. */
interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: Long): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(id: Long)
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>>
    suspend fun getLogDatesForHabit(habitId: Long): List<LocalDate>
    suspend fun insertLog(log: HabitLog)
    suspend fun deleteLog(habitId: Long, date: LocalDate)
    suspend fun hasLogForDate(habitId: Long, date: LocalDate): Boolean
}
