package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.HabitDao
import com.dailyfocus.data.local.dao.HabitLogDao
import com.dailyfocus.data.local.entity.HabitEntity
import com.dailyfocus.data.local.entity.HabitLogEntity
import com.dailyfocus.domain.model.Habit
import com.dailyfocus.domain.model.HabitLog
import com.dailyfocus.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val logDao: HabitLogDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getHabitById(id: Long): Habit? =
        habitDao.getById(id)?.toDomain()

    override suspend fun insertHabit(habit: Habit): Long =
        habitDao.insert(habit.toEntity())

    override suspend fun updateHabit(habit: Habit) =
        habitDao.update(habit.toEntity())

    override suspend fun deleteHabit(id: Long) =
        habitDao.delete(id)

    override fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>> =
        logDao.getByHabit(habitId).map { list -> list.map { it.toDomain() } }

    override suspend fun getLogDatesForHabit(habitId: Long): List<LocalDate> =
        logDao.getLogDatesForHabit(habitId)

    override suspend fun insertLog(log: HabitLog) =
        logDao.insert(log.toEntity())

    override suspend fun deleteLog(habitId: Long, date: LocalDate) =
        logDao.delete(habitId, date)

    override suspend fun hasLogForDate(habitId: Long, date: LocalDate): Boolean =
        logDao.hasLogForDate(habitId, date)
}

private fun HabitEntity.toDomain() = Habit(
    id = id, name = name, frequency = frequency,
    isActive = isActive, createdAt = createdAt, updatedAt = updatedAt
)

private fun Habit.toEntity() = HabitEntity(
    id = id, name = name, frequency = frequency,
    isActive = isActive, createdAt = createdAt, updatedAt = updatedAt
)

private fun HabitLogEntity.toDomain() = HabitLog(id = id, habitId = habitId, date = date)
private fun HabitLog.toEntity() = HabitLogEntity(id = id, habitId = habitId, date = date)
