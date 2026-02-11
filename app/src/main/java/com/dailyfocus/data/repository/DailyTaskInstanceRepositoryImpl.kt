package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.DailyTaskInstanceDao
import com.dailyfocus.data.local.entity.DailyTaskInstanceEntity
import com.dailyfocus.domain.model.DailyTaskInstance
import com.dailyfocus.domain.repository.DailyTaskInstanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DailyTaskInstanceRepositoryImpl @Inject constructor(
    private val dao: DailyTaskInstanceDao
) : DailyTaskInstanceRepository {

    override fun getByDate(date: LocalDate): Flow<List<DailyTaskInstance>> =
        dao.getByDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun getCompletedDatesForTask(recurringTaskId: Long): List<LocalDate> =
        dao.getCompletedDatesForTask(recurringTaskId)

    override suspend fun insertAll(instances: List<DailyTaskInstance>) =
        dao.insertAll(instances.map { it.toEntity() })

    override suspend fun toggleCompletion(id: Long, isCompleted: Boolean) =
        dao.toggleCompletion(id, isCompleted)

    override suspend fun existsForDate(recurringTaskId: Long, date: LocalDate): Boolean =
        dao.existsForDate(recurringTaskId, date)
}

private fun DailyTaskInstanceEntity.toDomain() = DailyTaskInstance(
    id = id, recurringTaskId = recurringTaskId, date = date,
    isCompleted = isCompleted, title = title, category = category
)

private fun DailyTaskInstance.toEntity() = DailyTaskInstanceEntity(
    id = id, recurringTaskId = recurringTaskId, date = date,
    isCompleted = isCompleted, title = title, category = category
)
