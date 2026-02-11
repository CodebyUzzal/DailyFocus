package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.RecurringTask
import kotlinx.coroutines.flow.Flow

/** Repository contract for [RecurringTask] definitions. */
interface RecurringTaskRepository {
    fun getAll(): Flow<List<RecurringTask>>
    fun getAllActive(): Flow<List<RecurringTask>>
    suspend fun getById(id: Long): RecurringTask?
    suspend fun insert(task: RecurringTask): Long
    suspend fun update(task: RecurringTask)
    suspend fun delete(id: Long)
}
