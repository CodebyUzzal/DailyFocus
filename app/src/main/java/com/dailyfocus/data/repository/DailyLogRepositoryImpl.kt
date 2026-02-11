package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.DailyLogEntryDao
import com.dailyfocus.data.local.entity.DailyLogEntryEntity
import com.dailyfocus.domain.model.DailyLogEntry
import com.dailyfocus.domain.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DailyLogRepositoryImpl @Inject constructor(
    private val dao: DailyLogEntryDao
) : DailyLogRepository {

    override fun getAll(): Flow<List<DailyLogEntry>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getByDate(date: LocalDate): Flow<List<DailyLogEntry>> =
        dao.getByDate(date).map { list -> list.map { it.toDomain() } }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyLogEntry>> =
        dao.getByDateRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun insert(entry: DailyLogEntry): Long =
        dao.insert(entry.toEntity())

    override suspend fun update(entry: DailyLogEntry) =
        dao.update(entry.toEntity())

    override suspend fun delete(id: Long) =
        dao.delete(id)
}

private fun DailyLogEntryEntity.toDomain() = DailyLogEntry(
    id = id, activityName = activityName, durationMinutes = durationMinutes,
    note = note, date = date, createdAt = createdAt
)

private fun DailyLogEntry.toEntity() = DailyLogEntryEntity(
    id = id, activityName = activityName, durationMinutes = durationMinutes,
    note = note, date = date, createdAt = createdAt
)
