package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllActive(): Flow<List<Routine>>
    fun getAll(): Flow<List<Routine>>
    suspend fun getById(id: Long): Routine?
    suspend fun insert(routine: Routine): Long
    suspend fun update(routine: Routine)
    suspend fun delete(id: Long)
}
