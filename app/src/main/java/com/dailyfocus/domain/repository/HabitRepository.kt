package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for [Habit]s and their completion logs. */
interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: Long): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(id: Long)
    suspend fun insertLog(habitId: Long, date: LocalDate)
    suspend fun deleteLog(habitId: Long, date: LocalDate)
    suspend fun hasLogForDate(habitId: Long, date: LocalDate): Boolean
    suspend fun getLogDatesForHabit(habitId: Long): List<LocalDate>
    fun getLogsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Pair<Long, LocalDate>>>
}
