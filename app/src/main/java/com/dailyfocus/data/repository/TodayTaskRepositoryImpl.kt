package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.TodayTaskDao
import com.dailyfocus.data.local.entity.TodayTaskEntity
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TodayTaskRepositoryImpl @Inject constructor(
    private val dao: TodayTaskDao
) : TodayTaskRepository {

    override fun getByDate(date: LocalDate): Flow<List<TodayTask>> =
        dao.getByDate(date).map { list -> list.map { it.toDomain() } }

    override fun getByDateAndCategory(date: LocalDate, category: String): Flow<List<TodayTask>> =
        dao.getByDateAndCategory(date, category).map { list -> list.map { it.toDomain() } }

    override suspend fun insert(task: TodayTask): Long =
        dao.insert(task.toEntity())

    override suspend fun update(task: TodayTask) =
        dao.update(task.toEntity())

    override suspend fun delete(id: Long) =
        dao.delete(id)

    override suspend fun existsForRecurringTaskAndDate(recurringTaskId: Long, date: LocalDate): Boolean =
        dao.existsForRecurringTaskAndDate(recurringTaskId, date)

    override fun getCompletedByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TodayTask>> =
        dao.getCompletedByDateRange(startDate, endDate).map { list -> list.map { it.toDomain() } }
}

private fun TodayTaskEntity.toDomain() = TodayTask(
    id = id, title = title, category = category,
    date = date, isCompleted = isCompleted,
    recurringTaskId = recurringTaskId, createdAt = createdAt
)

private fun TodayTask.toEntity() = TodayTaskEntity(
    id = id, title = title, category = category,
    date = date, isCompleted = isCompleted,
    recurringTaskId = recurringTaskId, createdAt = createdAt
)
