package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.RecurringTaskDao
import com.dailyfocus.data.local.entity.RecurringTaskEntity
import com.dailyfocus.domain.model.RecurringTask
import com.dailyfocus.domain.repository.RecurringTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecurringTaskRepositoryImpl @Inject constructor(
    private val dao: RecurringTaskDao
) : RecurringTaskRepository {

    override fun getAll(): Flow<List<RecurringTask>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getAllActive(): Flow<List<RecurringTask>> =
        dao.getAllActive().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): RecurringTask? =
        dao.getById(id)?.toDomain()

    override suspend fun insert(task: RecurringTask): Long =
        dao.insert(task.toEntity())

    override suspend fun update(task: RecurringTask) {
        dao.update(task.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }
}

private fun RecurringTaskEntity.toDomain() = RecurringTask(
    id = id, title = title, category = category,
    daysOfWeek = daysOfWeek, isActive = isActive, createdAt = createdAt
)

private fun RecurringTask.toEntity() = RecurringTaskEntity(
    id = id, title = title, category = category,
    daysOfWeek = daysOfWeek, isActive = isActive, createdAt = createdAt
)
