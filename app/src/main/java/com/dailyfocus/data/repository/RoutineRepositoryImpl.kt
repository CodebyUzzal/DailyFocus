package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.RoutineDao
import com.dailyfocus.data.local.entity.RoutineEntity
import com.dailyfocus.domain.model.Routine
import com.dailyfocus.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val dao: RoutineDao
) : RoutineRepository {

    override fun getAllActive(): Flow<List<Routine>> =
        dao.getAllActive().map { list -> list.map { it.toDomain() } }

    override fun getAll(): Flow<List<Routine>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Routine? =
        dao.getById(id)?.toDomain()

    override suspend fun insert(routine: Routine): Long =
        dao.insert(routine.toEntity())

    override suspend fun update(routine: Routine) {
        dao.update(routine.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }
}

// ── Mappers ─────────────────────────────────────────────────────────────

private fun RoutineEntity.toDomain() = Routine(
    id = id, title = title, category = category,
    currentStreak = currentStreak, longestStreak = longestStreak,
    lastCompletedDate = lastCompletedDate,
    reminderTime = reminderTime,
    isActive = isActive, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)

private fun Routine.toEntity() = RoutineEntity(
    id = id, title = title, category = category,
    currentStreak = currentStreak, longestStreak = longestStreak,
    lastCompletedDate = lastCompletedDate,
    reminderTime = reminderTime,
    isActive = isActive, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)
